import { marked } from 'marked';
import sanitizeHtml from 'sanitize-html';

const allowedTags = [
    ...sanitizeHtml.defaults.allowedTags,
    'img',
    'h1',
    'h2',
    'h3',
    'h4',
    'h5',
    'h6',
];

export async function renderSafeMarkdown(markdown) {
    const rendered = await marked.parse(markdown);

    return sanitizeHtml(rendered, {
        allowedTags,
        allowedAttributes: {
            a: ['href', 'name', 'target', 'title', 'rel'],
            img: ['src', 'alt', 'title', 'width', 'height'],
            code: ['class'],
        },
        allowedSchemes: ['http', 'https', 'mailto'],
        allowedSchemesByTag: {
            img: ['http', 'https'],
        },
        allowProtocolRelative: false,
        transformTags: {
            a: sanitizeHtml.simpleTransform('a', {
                rel: 'noopener noreferrer',
            }),
        },
    });
}
