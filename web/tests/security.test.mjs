import assert from 'node:assert/strict';
import test from 'node:test';

import { renderSafeMarkdown } from '../src/utils/markdown.mjs';

test('removes active HTML and event handlers from Markdown', async () => {
    const html = await renderSafeMarkdown(
        '<script>alert(1)</script><img src="https://example.com/a.png" onerror="alert(2)">',
    );

    assert.doesNotMatch(html, /<script|onerror/i);
    assert.match(html, /<img src="https:\/\/example\.com\/a\.png"/);
});

test('removes executable and protocol-relative URLs', async () => {
    const html = await renderSafeMarkdown(
        '[bad](javascript:alert(1)) [external](//evil.example/path)',
    );

    assert.doesNotMatch(html, /javascript:|\/\/evil\.example/i);
});

test('hardens links emitted from Markdown', async () => {
    const html = await renderSafeMarkdown('[safe](https://example.com)');

    assert.match(html, /rel="noopener noreferrer"/);
});
