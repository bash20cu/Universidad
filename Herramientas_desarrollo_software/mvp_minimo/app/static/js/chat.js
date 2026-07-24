const form = document.querySelector("#chat-form");
const promptInput = document.querySelector("#prompt");
const messagesElement = document.querySelector("#messages");
const sendButton = document.querySelector("#send-button");
const characterCount = document.querySelector("#character-count");
const headerStatus = document.querySelector("#header-status");
const refreshButton = document.querySelector("#refresh-status");
const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

const conversation = [];
let streaming = false;

document.querySelectorAll("[data-prompt]").forEach((button) => {
  button.addEventListener("click", () => {
    promptInput.value = button.dataset.prompt;
    updateComposer();
    promptInput.focus();
  });
});

promptInput.addEventListener("input", updateComposer);
promptInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter" && !event.shiftKey) {
    event.preventDefault();
    form.requestSubmit();
  }
});

refreshButton.addEventListener("click", refreshStatus);

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const content = promptInput.value.trim();
  if (!content || streaming) return;

  appendMessage("user", content);
  conversation.push({ role: "user", content });
  promptInput.value = "";
  updateComposer();

  const assistant = appendMessage("assistant", "");
  const textElement = assistant.querySelector(".markdown-body");
  const responseMeta = assistant.querySelector("[data-response-meta]");
  textElement.classList.add("stream-cursor");
  setStreaming(true);

  try {
    const response = await fetch("/chat/api/chat", {
      method: "POST",
      headers: { "Content-Type": "application/json", "X-CSRFToken": csrfToken },
      body: JSON.stringify({ messages: conversation }),
    });

    if (!response.ok) {
      const payload = await response.json();
      throw new Error(payload.error || "No fue posible iniciar la respuesta.");
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    let buffer = "";
    let completeText = "";

    while (true) {
      const { value, done } = await reader.read();
      if (done) break;

      buffer += decoder.decode(value, { stream: true });
      const events = buffer.split("\n\n");
      buffer = events.pop() || "";

      for (const eventBlock of events) {
        const parsed = parseSSE(eventBlock);
        if (!parsed) continue;
        if (parsed.done) continue;
        if (parsed.error) throw new Error(parsed.error);
        if (parsed.meta) {
          updateResponseMeta(responseMeta, parsed.meta);
          continue;
        }
        if (parsed.content) {
          completeText += parsed.content;
          renderMessageContent(textElement, completeText, "assistant");
          scrollToBottom();
        }
      }
    }

    if (!completeText.trim()) {
      throw new Error("El modelo terminó sin producir contenido.");
    }
    conversation.push({ role: "assistant", content: completeText });
  } catch (error) {
    assistant.classList.add("error-message");
    renderMessageContent(textElement, error.message, "error");
  } finally {
    textElement.classList.remove("stream-cursor");
    setStreaming(false);
    refreshStatus();
    promptInput.focus();
  }
});

function parseSSE(block) {
  const lines = block.split("\n");
  const eventName = lines
    .find((line) => line.startsWith("event:"))
    ?.slice(6)
    .trim();
  const data = lines
    .filter((line) => line.startsWith("data:"))
    .map((line) => line.slice(5).trim())
    .join("\n");

  if (!data) return null;
  if (data === "[DONE]") return { done: true };

  const payload = JSON.parse(data);
  if (eventName === "error" || payload.error) {
    return { error: payload.error || "Error del servidor FM." };
  }

  return {
    meta: eventName === "meta" ? payload : null,
    content: payload.choices?.[0]?.delta?.content || "",
  };
}

function appendMessage(role, content) {
  const article = document.createElement("article");
  article.className = `message ${role}-message`;

  const avatar = document.createElement("div");
  avatar.className = "avatar";
  avatar.setAttribute("aria-hidden", "true");
  avatar.textContent = role === "assistant" ? "IA" : "Tú";

  const messageContent = document.createElement("div");
  messageContent.className = "message-content";

  const meta = document.createElement("span");
  meta.className = "message-meta";
  meta.textContent = `${role === "assistant" ? "TutorIA" : "Tú"} · ahora`;

  const body = document.createElement("div");
  body.className = role === "assistant" ? "markdown-body" : "plain-body";
  renderMessageContent(body, content, role);

  messageContent.append(meta, body);
  if (role === "assistant") {
    const responseMeta = document.createElement("div");
    responseMeta.dataset.responseMeta = "true";
    responseMeta.textContent = "Preparando ficha técnica…";
    messageContent.append(responseMeta);
  }
  article.append(avatar, messageContent);
  messagesElement.append(article);
  scrollToBottom();
  return article;
}

function updateResponseMeta(element, metadata) {
  if (!element) return;
  const tokens = metadata.tokens;
  const tokenCopy = tokens
    ? `${tokens.total ?? "?"} tokens ${tokens.source}`
    : "calculando tokens";
  const timeCopy = metadata.elapsed_ms ? `${metadata.elapsed_ms} ms` : "en curso";
  element.textContent = `${metadata.model || "modelo desconocido"} · ${tokenCopy} · ${timeCopy} · ${metadata.network || "red no disponible"}`;
  element.title = `Proveedor: ${metadata.provider || "desconocido"} | Acceso: ${metadata.access_mode || "desconocido"}`;
}

function renderMessageContent(element, content, role) {
  if (role === "assistant") {
    element.innerHTML = renderMarkdown(content);
    return;
  }

  element.textContent = content;
}

function renderMarkdown(markdown) {
  const codeBlocks = [];
  let safeText = escapeHtml(markdown);

  // Los bloques de código se extraen primero para que el resto del parser no altere su contenido.
  safeText = safeText.replace(/```([\w+-]*)\n?([\s\S]*?)```/g, (_match, language, code) => {
    const index = codeBlocks.length;
    const label = language ? `<span>${language}</span>` : "";
    codeBlocks.push(`<pre><code data-language="${language || "texto"}">${code.trim()}</code>${label}</pre>`);
    return `\n@@CODE_BLOCK_${index}@@\n`;
  });

  const lines = safeText.split("\n");
  const html = [];
  let paragraph = [];
  let listItems = [];

  const flushParagraph = () => {
    if (!paragraph.length) return;
    html.push(`<p>${renderInline(paragraph.join(" ").trim())}</p>`);
    paragraph = [];
  };

  const flushList = () => {
    if (!listItems.length) return;
    html.push(`<ul>${listItems.map((item) => `<li>${renderInline(item)}</li>`).join("")}</ul>`);
    listItems = [];
  };

  for (const line of lines) {
    const trimmed = line.trim();
    if (!trimmed) {
      flushParagraph();
      flushList();
      continue;
    }

    const codeMatch = trimmed.match(/^@@CODE_BLOCK_(\d+)@@$/);
    if (codeMatch) {
      flushParagraph();
      flushList();
      html.push(codeBlocks[Number(codeMatch[1])]);
      continue;
    }

    const heading = trimmed.match(/^(#{1,3})\s+(.+)$/);
    if (heading) {
      flushParagraph();
      flushList();
      const level = heading[1].length + 1;
      html.push(`<h${level}>${renderInline(heading[2])}</h${level}>`);
      continue;
    }

    const bullet = trimmed.match(/^[-*]\s+(.+)$/);
    if (bullet) {
      flushParagraph();
      listItems.push(bullet[1]);
      continue;
    }

    paragraph.push(trimmed);
  }

  flushParagraph();
  flushList();
  return html.join("");
}

function renderInline(text) {
  return text
    .replace(/`([^`]+)`/g, "<code>$1</code>")
    .replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>")
    .replace(/\*([^*]+)\*/g, "<em>$1</em>");
}

function escapeHtml(value) {
  return value
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

window.TutorIAMarkdown = { render: renderMarkdown };

async function refreshStatus() {
  refreshButton.classList.add("spinning");
  try {
    let response = await fetch("/chat/api/status");
    let payload = await response.json();

    if (!payload.available) {
      response = await fetch("/chat/api/provider/wake", {
        method: "POST",
        headers: { "X-CSRFToken": csrfToken },
      });
      payload = await response.json();
      if (!response.ok) throw new Error(payload.error);
      payload = payload.status;
    }

    setHealthUI(payload);
  } catch (error) {
    setHealthUI({ available: false, model: "desconocido" });
  } finally {
    refreshButton.classList.remove("spinning");
  }
}

function setHealthUI(status) {
  const available = status.available;
  const model = status.model;
  const headerText = headerStatus.querySelector("span:last-child");
  headerStatus.classList.toggle("offline", !available);
  headerText.textContent = available
    ? "Proveedor IA disponible"
    : "Proveedor IA no disponible";

  document.querySelectorAll("[data-health-label]").forEach((label) => {
    label.textContent = available ? "En línea" : "Sin conexión";
    label.classList.toggle("healthy", available);
    label.classList.toggle("unhealthy", !available);
  });

  const modelLabel = document.querySelector("[data-model-label]");
  modelLabel.textContent = available ? `${model} disponible` : "No disponible";
  modelLabel.classList.toggle("healthy", available);
  modelLabel.classList.toggle("unhealthy", !available);
  document.querySelector("#health-copy").textContent = available
    ? "Todo en orden"
    : "Requiere atención";

  if (status.processing_location) {
    document.querySelector("#processing-location").textContent =
      formatLocation(status.processing_location);
  }
  if (status.access_mode) {
    document.querySelector("#access-mode").textContent =
      `Acceso ${formatLocation(status.access_mode)}`;
  }
  const runtimeOwner = document.querySelector("#runtime-owner");
  if (runtimeOwner && typeof status.managed_by_app === "boolean") {
    runtimeOwner.textContent = status.managed_by_app
      ? "Administrado por TutorIA"
      : "Detectado localmente";
  }

  const privacyCopy = document.querySelector("#privacy-copy");
  if (status.processing_location === "device") {
    privacyCopy.textContent =
      "El proveedor activo procesa las conversaciones en el dispositivo que ejecuta el modelo.";
  } else {
    privacyCopy.textContent =
      "El procesamiento puede ocurrir fuera del dispositivo del usuario. Aplican las políticas del proveedor activo.";
  }
}

function formatLocation(value) {
  const labels = {
    device: "En el dispositivo",
    local: "local",
    lan: "por red local",
    remote: "remoto",
    private_cloud: "Nube privada",
  };
  return labels[value] || value;
}

function setStreaming(active) {
  streaming = active;
  sendButton.disabled = active;
  promptInput.disabled = active;
}

function updateComposer() {
  characterCount.textContent = `${promptInput.value.length} / 4000`;
  promptInput.style.height = "auto";
  promptInput.style.height = `${Math.min(promptInput.scrollHeight, 150)}px`;
}

function scrollToBottom() {
  messagesElement.scrollTop = messagesElement.scrollHeight;
}

refreshStatus();
updateComposer();
