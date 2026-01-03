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

                // Estimate projects: subdirectories
                // Also could verify if they have READMEs
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

                if (isDirectory) {
                    hasReadme = fs.existsSync(path.join(itemPath, 'README.md'));
                }

                return {
                    name: item,
                    path: path.join(coursePath, item), // Store full relative path
                    type: isDirectory ? 'directory' : 'file',
                    hasReadme
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
            return fs.readFileSync(fullPath, 'utf-8');
        }

        // Try case insensitive check if exact match fails (linux is case sensitive but user usage might vary)
        // Usually it's README.md, but let's stick to that for now.
        return null;
    } catch (e) {
        console.error(`Error reading README for ${relativePath}:`, e);
        return null;
    }
}

// Special function for the root README
export function getRootReadme(): string | null {
    try {
        const rootDir = path.resolve('../');
        const fullPath = path.join(rootDir, 'README.md');
        if (fs.existsSync(fullPath)) {
            return fs.readFileSync(fullPath, 'utf-8');
        }
        return null;
    } catch (e) {
        console.error("Error reading root README:", e);
        return null;
    }
}
