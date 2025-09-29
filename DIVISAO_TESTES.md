#  Divisão dos Testes - Instruções para Trabalho em Dupla

##  **Divisão Planejada dos Testes**

###  **TESTES DO GUSTAVO (já implementados):**

| Arquivo | Testes | Tipo | Descrição |
|---------|--------|------|-----------|
| `BoletimServiceTest.java` | 11 | Mock/Unit | Testes principais do serviço |
| `AlunoRepositoryMockTest.java` | 4 | Mock | Testes do repositório de alunos |
| `NotasRepositoryMockTest.java` | 5 | Mock | Testes do repositório de notas |
| `PoliticaArredondamentoTest.java` | 3 | Unit | Testes de arredondamento |
| `PoliticaRecuperacaoTest.java` | 2 | Unit | Testes de política de recuperação |
| `AlunoTest.java` | 2 | Unit | Testes da entidade Aluno |
| `SituacaoAlunoTest.java` | 2 | Unit | Testes do enum SituacaoAluno |
| `SistemaNotasIntegrationTest.java` | 6 | Integration | Testes de integração |

** TOTAL Gustavo: 35 testes (29 unitários/mock + 6 integração) **

---

### **TESTES PARA O LUCCA (a serem implementados):**

| Arquivo | Testes | Tipo | Descrição |
|---------|--------|------|-----------|
| `BoletimServiceTest2.java` | 11+ | Mock/Unit | Testes avançados do serviço |
| `BoletimServiceTest3.java` | 10+ | Mock/Unit | Testes de casos especiais |
| `TestesAdicionais.java` | 10+ | Unit | Testes das classes auxiliares |

** META LUCCA: 30+ testes (todos unitários/mock)**

---

##  **Arquivos Modelo para o Lucca**

### **Sugestões de Testes para Implementar:**

#### **BoletimServiceTest2.java** - Testes avançados:
-  Configuração de pesos customizados
-  Salvamento de notas (P1, P2, RECUPERACAO)
-  Métodos utilitários (precisaRecuperacao, aprovadoDireto, reprovadoDireto)
-  Validações de entrada com diferentes cenários
-  Enum de situação (getSituacaoFinalEnum)

#### **BoletimServiceTest3.java** - Casos especiais:
-  Cálculos com recuperação (com e sem nota)
-  Casos limite (notas 0, 100, exatamente 30, 60, 50)
-  Pesos invertidos e configurações especiais
-  Situações de borda (média exatamente nos limites)

#### **TestesAdicionais.java** - Classes auxiliares:
-  Testes complementares de PoliticaArredondamento
-  Testes complementares de PoliticaRecuperacao
-  Testes complementares de SituacaoAluno
-  Validações adicionais da classe Aluno

---

##  **Status Atual do Projeto**

### **Gustavo - CONCLUÍDO:**
```bash
mvn test
# Resultado: 35 testes (29 unitários/mock)
# Status:  TODOS PASSANDO
```

###  **Lucca - PENDENTE:**
```bash  
# Precisa implementar ~30 testes adicionais
# Foco: testes unitários/mock
# Meta: >20 testes unitários/mock
```

##  **Cobertura Sugerida para o Lucca**

### **Áreas ainda não cobertas ou com cobertura parcial:**
1. **Cenários extremos** do BoletimService
2. **Validações de edge cases** 
3. **Testes de performance** de cálculos
4. **Casos de erro** não cobertos
5. **Integrações** entre componentes
6. **Configurações** não padrão

### **Exemplos de testes a implementar:**
- Notas com decimais complexos (ex: 59.999, 30.001)
- Recuperação com notas extremas (0, 100)
- Pesos não padrão (0.1/0.9, 0.3/0.7)
- Múltiplas chamadas sequenciais
- Estados inconsistentes

##  **Comando para Validar**

```bash
# Depois de implementar os testes
mvn clean test

# Meta: Tests run: 65+, Failures: 0, Errors: 0
```

##  **Divisão de Responsabilidades**

###  **Gustavo (Implementado):**
-  Estrutura base do sistema
-  Testes fundamentais 
-  Casos principais de uso
-  Repositórios e mocks básicos
-  Integração básica

###  **Lucca (A implementar):**
-  Testes avançados e edge cases
-  Cenários complexos
-  Validações adicionais
-  Casos de erro específicos
-  Configurações não padrão

---

** Cada desenvolvedor terá mais de 20 testes unitários/mock em commits separados!**