import fs from 'node:fs';
import path from 'node:path';

const IGNORED_DIRS = ['.git', '.vscode', 'node_modules', 'web', 'config', 'Powershell_Scripts', '.gemini', '.agent'];
const IGNORED_FILES = ['LICENSE', 'README.md', 'changelog.md', '.gitattributes', '.gitignore'];

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
}

export function getCourses(): Course[] {
    const rootDir = path.resolve('../'); // Parent of 'web'

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

                const count = contents.length;

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
            .filter(item => !IGNORED_FILES.includes(item) && !item.startsWith('.'))
            .map(item => {
                const itemPath = path.join(fullPath, item);
                const stats = fs.statSync(itemPath);
                return {
                    name: item,
                    path: path.join(coursePath, item), // Store full relative path for potential linking
                    type: stats.isDirectory() ? 'directory' : 'file'
                };
            });
    } catch (e) {
        console.error(`Error reading course content for ${coursePath}:`, e);
        return [];
    }
}
