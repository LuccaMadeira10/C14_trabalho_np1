#!/usr/bin/env bash
set -euo pipefail

# Falha se EMAIL_TO nao vier do Actions (variavel, nao secret)
: "${EMAIL_TO:?Variavel EMAIL_TO nao definida}"

# Coleta resumo dos testes do Surefire (se existir)
TEST_DIR="target/surefire-reports"
TOTAL_TESTS="0"
TOTAL_FAILS="0"
TOTAL_ERRORS="0"
TOTAL_SKIPPED="0"

if ls "$TEST_DIR"/TEST-*.xml >/dev/null 2>&1; then
  read -r TOTAL_TESTS TOTAL_FAILS TOTAL_ERRORS TOTAL_SKIPPED < <(
    awk -F\" '
      /<testsuite / {
        for(i=1;i<=NF;i++){
          if($i=="tests"){t+=$(i+1)}
          if($i=="failures"){f+=$(i+1)}
          if($i=="errors"){e+=$(i+1)}
          if($i=="skipped"){s+=$(i+1)}
        }
      }
      END { printf "%d %d %d %d\n", t, f, e, s }
    ' "$TEST_DIR"/TEST-*.xml
  )
fi

# Monta corpo do e-mail
cat > email-body.txt <<EOF
Pipeline: ${GITHUB_WORKFLOW:-}
Status: ${JOB_STATUS:-}
Repositorio: ${GITHUB_REPOSITORY:-}
Branch: ${GITHUB_REF_NAME:-}
Commit: ${GITHUB_SHA:-}
Run: ${GITHUB_RUN_NUMBER:-}

Resumo de testes (Surefire):
  Tests:   ${TOTAL_TESTS}
  Failures:${TOTAL_FAILS}
  Errors:  ${TOTAL_ERRORS}
  Skipped: ${TOTAL_SKIPPED}

Relatorios XML anexados (se presentes): ${TEST_DIR}/TEST-*.xml
EOF

echo "Email para: $EMAIL_TO"
echo "Corpo gerado em: email-body.txt"
