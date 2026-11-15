# Contexto
Trabalho AP2 de Desenvolvimento Mobile (entrega e apresentação em 16/11): app Android (Java/XML, módulo app/ na raiz) + API FastAPI + SQLite em backend/. Tema: CinePersona.
Fonte da verdade: docs/ESCOPO.md (prevalece), docs/REGRAS.md e docs/BRIEFING_ORIGINAL.md. Leia antes de qualquer tarefa. O que não está no ESCOPO não se implementa.
## Como trabalhar
- Windows, PowerShell. Gradle: `.\gradlew.bat`. Explique de forma simples e curta.
- Uma fase por vez. Ao terminar, rode `pytest -q` (em backend/) e `.\gradlew.bat testDebugUnitTest assembleDebug` e corrija até ficar verde.
- Commits só quando eu pedir: um assunto por commit, Conventional Commits em português, até 72 caracteres. Nunca faça push. Antes de commitar, mostre `git status --short` e confirme que não há arquivos gerados, segredos nem local.properties.
- Nunca altere local.properties. Sem mudanças fora do que a fase pede. Se o pedido divergir do ESCOPO, pare e pergunte.
## Estado
Backend completo com testes. Android: base + Fase 4 (Login e Questionário) e Fase 5 (Recomendações e Detalhes) concluídas, commitadas e com push. Faltam: celular físico (bônus), Fase 6 (Diário), teste manual completo no emulador, refatoração, README, auditoria.
