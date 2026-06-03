import { defineConfig } from 'vite'
import react, { reactCompilerPreset } from '@vitejs/plugin-react'
import babel from '@rolldown/plugin-babel'
import tailwindcss from '@tailwindcss/vite'
import path from 'path'

export default defineConfig({
  plugins: [
    react(),
    babel({ presets: [reactCompilerPreset()] }),
    tailwindcss(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    }
  },
  server: {
    proxy: {
      '/api/v1': { target: 'http://localhost:8080', changeOrigin: true },
      '/chat': { target: 'http://localhost:8080', changeOrigin: true },
      '/ws-stomp': { target: 'http://localhost:8080', ws: true },
    }
  }
})
