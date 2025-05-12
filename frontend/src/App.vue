<script setup>
import HelloWorld from './components/HelloWorld.vue'
import TheWelcome from './components/TheWelcome.vue'
</script>

<template>
  <div class="app">
    <nav class="navbar">
      <div class="container">
        <router-link to="/" class="logo">Job App</router-link>
        <div class="nav-links">
          <router-link to="/offres">Offres</router-link>
          <router-link to="/candidatures">Candidatures</router-link>
        </div>
      </div>
    </nav>

    <main class="container">
      <router-view></router-view>
    </main>
  </div>
</template>

<style>
.app {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.navbar {
  background-color: #2c3e50;
  padding: 1rem 0;
  margin-bottom: 2rem;
}

.navbar .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  color: white;
  font-size: 1.5rem;
  text-decoration: none;
  font-weight: bold;
}

.nav-links {
  display: flex;
  gap: 1rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.nav-links a:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-links a.router-link-active {
  background-color: rgba(255, 255, 255, 0.2);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}
</style>
