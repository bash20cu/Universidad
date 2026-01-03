import fs from 'node:fs';
import path from 'node:path';

const IGNORED_DIRS = ['.git', '.vscode', 'node_modules', 'web', 'config', 'Powershell_Scripts', '.gemini', '.agent'];
const IGNORED_FILES = ['LICENSE', '.gitattributes', '.gitignore'];

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
    const rootDir = path.resolve('../');

    try {
        const items = fs.readdirSync(rootDir);

        const courses = items
            .filter(item => {
                const fullPath = path.join(rootDir, item);
                return fs.statSync(fullPath).isDirectory() &&
                    !IGNORED_DIRS.includes(item) &&
                    !item.startsWith('.');
            })
            .map(dirName => {
                const fullPath = path.join(rootDir, dirName);
                const contents = fs.readdirSync(fullPath);

                const count = contents.filter(c => {
                    const cPath = path.join(fullPath, c);
                    return fs.statSync(cPath).isDirectory() && !c.startsWith('.');
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
    const rootDir = path.resolve('../');
    const fullPath = path.join(rootDir, coursePath);

    try {
        const items = fs.readdirSync(fullPath);
        return items
            .filter(item => !IGNORED_FILES.includes(item) && !item.startsWith('.') && item !== 'README.md')
            .map(item => {
                const itemPath = path.join(fullPath, item);
                const stats = fs.statSync(itemPath);
                const isDirectory = stats.isDirectory();
                let hasReadme = false;
                let description = undefined;
                let technologies: string[] = [];

                if (isDirectory) {
                    const readmePath = path.join(itemPath, 'README.md');
                    hasReadme = fs.existsSync(readmePath);

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
                    name: item,
                    path: path.join(coursePath, item),
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
        const rootDir = path.resolve('../');
        const fullPath = path.join(rootDir, relativePath, 'README.md');

        if (fs.existsSync(fullPath)) {
            let content = fs.readFileSync(fullPath, 'utf-8');

            // Rewrite relative image paths to absolute GitHub raw URLs
            // Matches ![alt](path) where path does not start with http or https
            content = content.replace(/!\[(.*?)\]\((?!http)(.*?)\)/g, (match, alt, imgPath) => {
                // Remove leading ./ or / if present to get clean relative path
                const cleanPath = imgPath.replace(/^\.?\//, '');
                // Construct absolute URL: root + relativePath + cleanPath
                // relativePath is like "Base_de_Datos/Base_Datos_1/Laboratorio_2"
                // We need to join them correctly
                const absoluteUrl = `https://raw.githubusercontent.com/bash20cu/Universidad/master/${relativePath}/${cleanPath}`;
                return `![${alt}](${absoluteUrl})`;
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
    const rootDir = path.resolve('../');
    const fullPath = path.join(rootDir, 'README.md');
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
