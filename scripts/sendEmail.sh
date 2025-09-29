#!/usr/bin/env bash
# Script para envio de email - Pipeline CI/CD
# Será implementado pelo Lucca
# 
# Variáveis de ambiente necessárias:
# - EMAIL_TO: Email do destinatário (não pode estar hardcoded)
# - PIPELINE_STATUS: Status da execução do pipeline
# - BUILD_URL: URL do build no GitHub Actions
# - COMMIT_SHA: Hash do commit
# - BRANCH_NAME: Nome da branch

echo "============================================"
echo "Script de Notificação por Email"
echo "============================================"

# Verificar se as variáveis de ambiente estão definidas
if [ -z "$EMAIL_TO" ]; then
    echo "ERRO: Variável EMAIL_TO não está definida!"
    echo "Defina a variável de ambiente EMAIL_TO no GitHub Actions"
    exit 1
fi

# Informações do pipeline
PIPELINE_STATUS=${PIPELINE_STATUS:-"unknown"}
BUILD_URL=${BUILD_URL:-"N/A"}
COMMIT_SHA=${COMMIT_SHA:-"N/A"}
BRANCH_NAME=${BRANCH_NAME:-"N/A"}
BUILD_DATE=$(date)

echo "Configurações:"
echo "- Email destinatário: $EMAIL_TO"
echo "- Status do pipeline: $PIPELINE_STATUS"
echo "- URL do build: $BUILD_URL"
echo "- Commit: $COMMIT_SHA"
echo "- Branch: $BRANCH_NAME"
echo "- Data: $BUILD_DATE"
echo ""

# TODO: Implementar pelo Lucca
# Aqui deve ser implementada a lógica de envio de email
# Sugestões de implementação:
# 1. Usar sendmail
# 2. Usar curl com API de email (SendGrid, Mailgun, etc.)
# 3. Usar SMTP simples
# 4. Usar algum serviço de webhook

echo "TODO: Implementar envio de email"
echo "Mensagem que seria enviada:"
echo "----------------------------"
echo "Assunto: Pipeline executado - $PIPELINE_STATUS"
echo "Para: $EMAIL_TO"
echo ""
echo "Olá!"
echo ""
echo "O pipeline do projeto Sistema de Notas Inatel foi executado."
echo ""
echo "Detalhes da execução:"
echo "- Status: $PIPELINE_STATUS"
echo "- Branch: $BRANCH_NAME"
echo "- Commit: $COMMIT_SHA"
echo "- Data/Hora: $BUILD_DATE"
echo "- Build URL: $BUILD_URL"
echo ""
echo "Atenciosamente,"
echo "Sistema de CI/CD"
echo "----------------------------"

# Retornar sucesso por enquanto
echo "Script executado com sucesso (modo simulação)"
echo "Aguardando implementação pelo Lucca"
exit 0
