import fs from 'node:fs';
import path from 'node:path';

const IGNORED_DIRS = ['.git', '.vscode', 'node_modules', 'web', 'config', '.gemini', '.agent'];
const IGNORED_FILES = ['LICENSE', '.gitattributes', '.gitignore'];
const ROOT_DIR = path.resolve(process.cwd(), '..');
const ROOT_REAL_PATH = fs.realpathSync(ROOT_DIR);

function isWithinRoot(candidatePath: string): boolean {
    const relative = path.relative(ROOT_REAL_PATH, candidatePath);
    return relative === '' || (!relative.startsWith(`..${path.sep}`) && relative !== '..' && !path.isAbsolute(relative));
}

function resolveExistingWithinRoot(...segments: string[]): string {
    const candidate = path.resolve(ROOT_DIR, ...segments);
    const lexicalRelative = path.relative(ROOT_DIR, candidate);

    if (lexicalRelative === '..' || lexicalRelative.startsWith(`..${path.sep}`) || path.isAbsolute(lexicalRelative)) {
        throw new Error('Path escapes the repository root.');
    }

    const realPath = fs.realpathSync(candidate);
    if (!isWithinRoot(realPath)) {
        throw new Error('Resolved path escapes the repository root.');
    }

    return realPath;
}

function isSafeRouteSegment(segment: string): boolean {
    return segment !== '' &&
        segment !== '.' &&
        segment !== '..' &&
        !segment.includes('/') &&
        !segment.includes('\\');
}

function toGitHubRawUrl(relativePath: string, imagePath: string): string | null {
    const normalizedBase = relativePath.split(path.sep).join('/');
    const normalizedImage = imagePath.replaceAll('\\', '/').replace(/^\.?\//, '');
    const combined = path.posix.normalize(path.posix.join(normalizedBase, normalizedImage));

    if (combined === '..' || combined.startsWith('../') || path.posix.isAbsolute(combined)) {
        return null;
    }

    const encodedPath = combined.split('/').map(encodeURIComponent).join('/');
    return `https://raw.githubusercontent.com/bash20cu/Universidad/main/${encodedPath}`;
}

export interface Course {
    name: string;
    path: string;
    description?: string;
    projectCount: number;
}

export interface Project {
    name: string;
    path: string;
    type: 'file' | 'directory';
    hasReadme: boolean;
    description?: string;
    technologies?: string[];
}

export interface ReadmeData {
    raw: string;
    about?: string;
    technologies?: string[]; // Array of image URLs
    stats?: string[]; // Array of image URLs
    intro?: string;
}

export function getCourses(): Course[] {
    try {
        const items = fs.readdirSync(ROOT_REAL_PATH, { withFileTypes: true });

        const courses = items
            .filter(item => {
                return item.isDirectory() &&
                    !item.isSymbolicLink() &&
                    isSafeRouteSegment(item.name) &&
                    !IGNORED_DIRS.includes(item.name) &&
                    !item.name.startsWith('.');
            })
            .map(item => {
                const dirName = item.name;
                const fullPath = resolveExistingWithinRoot(dirName);
                const contents = fs.readdirSync(fullPath, { withFileTypes: true });

                const count = contents.filter(c => {
                    return c.isDirectory() &&
                        !c.isSymbolicLink() &&
                        isSafeRouteSegment(c.name) &&
                        !c.name.startsWith('.');
                }).length;

                return {
                    name: dirName.replace(/_/g, ' '),
                    path: dirName,
                    projectCount: count
                };
            });

        return courses;
    } catch (e) {
        console.error("Error reading repository structure:", e);
        return [];
    }
}

export function getCourseContent(coursePath: string): Project[] {
    try {
        if (!isSafeRouteSegment(coursePath)) {
            return [];
        }

        const fullPath = resolveExistingWithinRoot(coursePath);
        const items = fs.readdirSync(fullPath, { withFileTypes: true });

        return items
            .filter(item =>
                !item.isSymbolicLink() &&
                isSafeRouteSegment(item.name) &&
                !IGNORED_FILES.includes(item.name) &&
                !item.name.startsWith('.') &&
                item.name !== 'README.md'
            )
            .map(item => {
                const itemName = item.name;
                const itemPath = resolveExistingWithinRoot(coursePath, itemName);
                const isDirectory = item.isDirectory();
                let hasReadme = false;
                let description = undefined;
                let technologies: string[] = [];

                if (isDirectory) {
                    const readmePath = path.join(itemPath, 'README.md');
                    hasReadme = fs.existsSync(readmePath) &&
                        !fs.lstatSync(readmePath).isSymbolicLink() &&
                        isWithinRoot(fs.realpathSync(readmePath));

                    if (hasReadme) {
                        try {
                            const content = fs.readFileSync(readmePath, 'utf-8');

                            // Extract Description: First paragraph that is not a header or image/badge
                            const lines = content.split('\n');
                            for (const line of lines) {
                                const trimmed = line.trim();
                                if (trimmed.length > 0 && !trimmed.startsWith('#') && !trimmed.startsWith('![') && !trimmed.startsWith('<')) {
                                    description = trimmed;
                                    break;
                                }
                            }

                            // Extract Images (Potential technologies)
                            const imgRegex = /!\[.*?\]\((.*?)\)/g;
                            let match;
                            while ((match = imgRegex.exec(content)) !== null) {
                                // Filter for likely badge URLs or small icons if possible, but taking all for now allows user to see what they put
                                if (match[1].includes('shield') || match[1].includes('badge') || match[1].includes('logo')) {
                                    technologies.push(match[1]);
                                }
                            }
                            // Dedup
                            technologies = [...new Set(technologies)];
                        } catch (e) {
                            console.error("Error parsing project README:", e);
                        }
                    }
                }

                return {
                    name: itemName,
                    path: path.join(coursePath, itemName),
                    type: isDirectory ? 'directory' : 'file',
                    hasReadme,
                    description,
                    technologies
                };
            });
    } catch (e) {
        console.error(`Error reading course content for ${coursePath}:`, e);
        return [];
    }
}

export function getReadmeContent(relativePath: string): string | null {
    try {
        const segments = relativePath.split(path.sep);
        if (segments.some(segment => !isSafeRouteSegment(segment))) {
            return null;
        }

        const fullPath = resolveExistingWithinRoot(...segments, 'README.md');

        if (!fs.lstatSync(fullPath).isSymbolicLink()) {
            let content = fs.readFileSync(fullPath, 'utf-8');

            // Rewrite relative image paths to absolute GitHub raw URLs
            // Matches ![alt](path) where path does not start with http or https
            content = content.replace(/!\[(.*?)\]\((?!http)(.*?)\)/g, (match, alt, imgPath) => {
                const absoluteUrl = toGitHubRawUrl(relativePath, imgPath);
                return absoluteUrl ? `![${alt}](${absoluteUrl})` : '';
            });

            // Rewrite HTML img tags with relative paths
            content = content.replace(/<img(.*?)src=["'](?!http)(.*?)["'](.*?)>/g, (match, before, imgPath, after) => {
                const absoluteUrl = toGitHubRawUrl(relativePath, imgPath);
                return absoluteUrl ? `<img${before}src="${absoluteUrl}"${after}>` : '';
            });

            return content;
        }
        return null;
    } catch (e) {
        console.error(`Error reading README for ${relativePath}:`, e);
        return null;
    }
}

export function getParsedRootReadme(): ReadmeData {
    const fullPath = resolveExistingWithinRoot('README.md');
    let raw = '';

    if (fs.existsSync(fullPath)) {
        raw = fs.readFileSync(fullPath, 'utf-8');
    }

    const techRegex = /### Tecnologías Utilizadas\s+([\s\S]*?)(?=###|$)/;
    const techMatch = raw.match(techRegex);
    const technologies: string[] = [];
    if (techMatch) {
        const techSection = techMatch[1];
        const imgRegex = /!\[.*?\]\((.*?)\)/g;
        let match;
        while ((match = imgRegex.exec(techSection)) !== null) {
            technologies.push(match[1]);
        }
    }

    const statsRegex = /### Estadísticas del Repositorio\s+([\s\S]*?)(?=###|$)/;
    const statsMatch = raw.match(statsRegex);
    const stats: string[] = [];
    if (statsMatch) {
        const statsSection = statsMatch[1];
        const imgRegex = /!\[.*?\]\((.*?)\)/g;
        let match;
        while ((match = imgRegex.exec(statsSection)) !== null) {
            stats.push(match[1]);
        }
    }

    const aboutRegex = /¡Bienvenido([\s\S]*?)(?=## Acerca|## Licencia|$)/;
    const aboutMatch = raw.match(aboutRegex);
    const intro = aboutMatch ? "¡Bienvenido" + aboutMatch[1].trim() : "";

    return {
        raw,
        technologies,
        stats,
        intro
    };
}
