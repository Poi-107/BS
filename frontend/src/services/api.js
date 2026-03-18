const TOKEN_KEY = "bs_token";

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || "";
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

async function request(path, options = {}) {
  const headers = options.headers || {};
  const token = getToken();
  if (token) {
    headers.token = token;
    headers["bs_token"] = token;
  }
  const res = await fetch(path, {
    credentials: "include",
    ...options,
    headers
  });
  const json = await res.json().catch(() => ({ code: "0", message: "Invalid_JSON" }));
  if (json && json.code === "0" && json.message === "Not_Login") {
    clearToken();
    throw new Error("Not_Login");
  }
  return json;
}

export async function apiGet(path) {
  return request(path, { method: "GET" });
}

export async function apiPost(path, data) {
  return request(path, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
}

export async function apiUpload(path, formData) {
  return request(path, {
    method: "POST",
    body: formData
  });
}
