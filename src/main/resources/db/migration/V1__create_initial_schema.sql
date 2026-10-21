CREATE TABLE unidade_fatec (
    id_unidade_fatec BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_unidade VARCHAR(100) NOT NULL,
    cep VARCHAR(10),
    logradouro VARCHAR(200),
    municipio VARCHAR(100),
    uf VARCHAR(2)
);

CREATE TABLE curso (
    id_curso BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_curso VARCHAR(100) NOT NULL
);

CREATE TABLE oferta_curso (
    id_oferta_curso BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_unidade_fatec BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    turno VARCHAR(20) NOT NULL,
    duracao INT NOT NULL,
    CONSTRAINT fk_oferta_unidade FOREIGN KEY (id_unidade_fatec) REFERENCES unidade_fatec(id_unidade_fatec),
    CONSTRAINT fk_oferta_curso FOREIGN KEY (id_curso) REFERENCES curso(id_curso),
    CONSTRAINT uk_oferta_unidade_curso_turno UNIQUE (id_unidade_fatec, id_curso, turno)
);

CREATE TABLE aluno (
    id_aluno BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_oferta_curso BIGINT NOT NULL,
    ra VARCHAR(20),
    nome VARCHAR(200) NOT NULL,
    genero VARCHAR(30),
    etnia VARCHAR(30),
    data_nascimento DATE,
    endereco VARCHAR(200),
    pcd BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_aluno_oferta FOREIGN KEY (id_oferta_curso) REFERENCES oferta_curso(id_oferta_curso),
    CONSTRAINT uk_aluno_ra UNIQUE (ra)
);

CREATE TABLE perfil_estudantil (
    id_perfil_estudantil BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_aluno BIGINT NOT NULL,
    qtd_tentativa INT,
    realizou_cursinho BOOLEAN,
    tipo_escola VARCHAR(20),
    faixa_renda_familiar VARCHAR(50),
    contribuicao_renda_familiar VARCHAR(100),
    ciencia_vestibular VARCHAR(200),
    CONSTRAINT fk_perfil_aluno FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno),
    CONSTRAINT uk_perfil_aluno UNIQUE (id_aluno)
);

CREATE TABLE acompanhamento_academico (
    id_acompanhamento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_aluno BIGINT NOT NULL,
    periodo_letivo VARCHAR(10) NOT NULL,
    qtd_disciplina_cursada INT NOT NULL DEFAULT 0,
    qtd_disciplina_trancada INT NOT NULL DEFAULT 0,
    qtd_trancamento_curso INT NOT NULL DEFAULT 0,
    intencao_trancar_curso BOOLEAN,
    observacao_conciliacao VARCHAR(200),
    CONSTRAINT fk_acompanhamento_aluno FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno),
    CONSTRAINT uk_acompanhamento_aluno_periodo UNIQUE (id_aluno, periodo_letivo)
);

CREATE TABLE pesquisa_estagio (
    id_pesquisa_estagio BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_aluno BIGINT NOT NULL,
    data_coleta DATE DEFAULT (CURRENT_DATE),
    qtd_estagio_realizado INT DEFAULT 0,
    tempo_procurando_estagio INT,
    observacoes VARCHAR(200),
    CONSTRAINT fk_pesquisa_aluno FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno)
);

CREATE TABLE empresa (
    id_empresa BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(18),
    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(200),
    segmento_mercado VARCHAR(100),
    area_atuacao VARCHAR(100),
    email VARCHAR(100),
    cep_empresa VARCHAR(10),
    endereco VARCHAR(200),
    linkedin VARCHAR(100),
    CONSTRAINT uk_empresa_cnpj UNIQUE (cnpj)
);

CREATE TABLE contrato_estagio (
    id_contrato_estagio BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_aluno BIGINT NOT NULL,
    id_empresa BIGINT NOT NULL,
    area_atuacao VARCHAR(100),
    cargo_estagio VARCHAR(100),
    data_inicio DATE NOT NULL,
    data_fim_prevista DATE,
    situacao_contrato VARCHAR(30) NOT NULL,
    modalidade_trabalho VARCHAR(30),
    carga_horaria_semanal INT,
    valor_bolsa_mensal DECIMAL(10,2),
    auxilio_transporte BOOLEAN,
    flexibilidade_horario BOOLEAN,
    atividades_desenvolvidas VARCHAR(200),
    CONSTRAINT fk_contrato_aluno FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno),
    CONSTRAINT fk_contrato_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
);

CREATE INDEX idx_aluno_oferta ON aluno(id_oferta_curso);
CREATE INDEX idx_pesquisa_aluno ON pesquisa_estagio(id_aluno);
CREATE INDEX idx_pesquisa_data ON pesquisa_estagio(data_coleta);
CREATE INDEX idx_contrato_aluno ON contrato_estagio(id_aluno);
CREATE INDEX idx_contrato_empresa ON contrato_estagio(id_empresa);
CREATE INDEX idx_acompanhamento_aluno ON acompanhamento_academico(id_aluno);
