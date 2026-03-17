import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import InventoryView from "../views/InventoryView.vue";
import InboundView from "../views/InboundView.vue";
import OutboundView from "../views/OutboundView.vue";
import AuditView from "../views/AuditView.vue";
import LogView from "../views/LogView.vue";
import LoginView from "../views/LoginView.vue";

const routes = [
  { path: "/", redirect: "/login" },
  { path: "/login", component: LoginView },
  { path: "/home", component: HomeView },
  { path: "/inventory", component: InventoryView },
  { path: "/inbound", component: InboundView },
  { path: "/outbound", component: OutboundView },
  { path: "/audit", component: AuditView },
  { path: "/log", component: LogView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("bs_token");
  if (!token && to.path !== "/login") {
    next("/login");
    return;
  }
  if (token && to.path === "/login") {
    next("/home");
    return;
  }
  next();
});

export default router;
