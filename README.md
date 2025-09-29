# Sistema de Notas INATEL

Sistema desenvolvido para gerenciar as notas dos alunos do Instituto Nacional de Telecomunicações (INATEL), implementando as regras de aprovação, recuperação e reprovação conforme o regulamento acadêmico.

## 📋 Regras do Sistema

### Situação Parcial (sem recuperação)
- **APROVADO**: Média ≥ 60
- **RECUPERAÇÃO**: 30 ≤ Média < 60  
- **REPROVADO DIRETO**: Média < 30

### Situação Final (pós-recuperação)
- **APROVADO**: Média final ≥ 50
- **REPROVADO**: Média final < 50

### Cálculo das Médias
- **Média Parcial**: P1 × 40% + P2 × 60%
- **Média Final**: (Média Parcial + Nota Recuperação) ÷ 2

## 🏗️ Arquitetura

O sistema segue princípios de **Clean Architecture** com separação clara de responsabilidades:

```
src/main/java/br/inatel/cdg/boletim/
├── Aluno.java                    # Entidade aluno
├── Avaliacao.java               # Enum das avaliações (P1, P2, RECUPERACAO)
├── SituacaoAluno.java           # Enum das situações do aluno
├── BoletimService.java          # Serviço principal (lógica de negócio)
├── PoliticaRecuperacao.java     # Política de cálculo da recuperação
├── PoliticaArredondamento.java  # Política de arredondamento
├── SistemaNotasInatel.java      # Classe principal para demonstração
├── exceptions/
│   ├── AlunoNaoEncontradoException.java
│   └── NotaInvalidaException.java
└── repository/
    ├── AlunoRepository.java     # Interface do repositório de alunos
    ├── NotasRepository.java     # Interface do repositório de notas
    └── impl/
        ├── AlunoRepositoryImpl.java
        └── NotasRepositoryImpl.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven 3.8+

### Compilar e Testar
```bash
mvn clean compile test
```

### Executar a Aplicação
```bash
java -cp target/classes br.inatel.cdg.boletim.SistemaNotasInatel
```

## 📊 Exemplo de Execução

```
=== SISTEMA DE NOTAS INATEL ===
Regras:
- Nota >= 60: APROVADO
- 30 <= Nota < 60: RECUPERAÇÃO  
- Nota < 30: REPROVADO DIRETO
- Para passar na recuperação: média final >= 50

--- João Silva (ID: 123) ---
Média sem recuperação: 76,0
Situação parcial: APROVADO
Situação final: APROVADO

--- Maria Santos (ID: 456) ---
Média sem recuperação: 52,0
Situação parcial: RECUPERACAO
Média pós-recuperação: 56,0
Situação final: APROVADO

--- Pedro Oliveira (ID: 789) ---
Média sem recuperação: 26,0
Situação parcial: REPROVADO_DIRETO
Situação final: REPROVADO
```

## 🧪 Testes

O projeto inclui **25 testes** divididos em:

- **Testes Unitários**: Validam cada componente isoladamente usando Mockito
- **Testes de Integração**: Validam o fluxo completo do sistema
- **Testes de Casos Limite**: Verificam cenários extremos (notas 60.0, 50.0, etc.)

### Executar Testes
```bash
mvn test
```

### Relatório de Testes
```bash
mvn surefire-report:report
# Relatório gerado em: target/site/surefire-report.html
```

## 📚 Funcionalidades Principais

### BoletimService
```java
// Calcular média sem recuperação
double media = service.calcularMediaSemRecuperacao("123");

// Verificar situação parcial
String situacao = service.situacaoParcial("123");

// Calcular média pós-recuperação
double mediaFinal = service.calcularMediaPosRecuperacao("123");

// Verificar situação final
String situacaoFinal = service.situacaoFinal("123");

// Salvar notas
service.salvarNota("123", Avaliacao.P1, 75.0);
service.salvarNota("123", Avaliacao.P2, 85.0);
service.salvarNota("123", Avaliacao.RECUPERACAO, 60.0);

// Métodos utilitários
boolean precisaRec = service.precisaRecuperacao("123");
boolean aprovadoDireto = service.aprovadoDireto("123");
boolean reprovadoDireto = service.reprovadoDireto("123");
```

## 🔧 Configurações

### Personalizar Pesos das Avaliações
```java
BoletimService service = new BoletimService(alunoRepo, notasRepo);
service.configurarPesos(0.3, 0.7); // P1=30%, P2=70%
```

## 🎯 Casos de Uso Validados

✅ **Aprovação Direta**: Média 76.0 (P1=70, P2=80)  
✅ **Recuperação → Aprovação**: Média 52.0 + Rec 60.0 = Final 56.0  
✅ **Recuperação → Reprovação**: Média 52.0 + Rec 30.0 = Final 41.0  
✅ **Reprovação Direta**: Média 26.0 (P1=20, P2=30)  
✅ **Casos Limite**: Médias exatas 60.0 e 50.0  

## 🛡️ Validações Implementadas

- **Notas válidas**: 0.0 ≤ nota ≤ 100.0
- **Aluno existe**: Validação via repositório
- **Notas obrigatórias**: P1 e P2 são necessárias
- **Arredondamento**: Uma casa decimal (HALF_UP)

## 🏆 Qualidade do Código

- **Clean Code**: Métodos pequenos e bem nomeados
- **SOLID**: Princípios de design respeitados
- **Testabilidade**: Interfaces e injeção de dependência
- **Documentação**: Comentários claros e README completo

## 📝 Próximas Melhorias

- [ ] Persistência em banco de dados
- [ ] API REST para integração
- [ ] Interface web
- [ ] Relatórios em PDF
- [ ] Sistema de notificações

---

**Desenvolvido para o Inatel - Instituto Nacional de Telecomunicações**  
**Trabalho de Lucca, Pedro!**

