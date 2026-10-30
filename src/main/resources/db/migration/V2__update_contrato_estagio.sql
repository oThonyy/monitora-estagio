ALTER TABLE contrato_estagio MODIFY COLUMN id_empresa BIGINT NULL;
ALTER TABLE contrato_estagio MODIFY COLUMN data_inicio DATE NULL;
ALTER TABLE contrato_estagio ADD COLUMN funcao VARCHAR(100);
ALTER TABLE contrato_estagio ADD COLUMN curso VARCHAR(100);
ALTER TABLE contrato_estagio ADD COLUMN periodo_curso VARCHAR(50);
ALTER TABLE contrato_estagio ADD COLUMN horario_curso VARCHAR(50);
ALTER TABLE contrato_estagio ADD COLUMN tipo_contrato VARCHAR(30);
ALTER TABLE contrato_estagio ADD COLUMN possui_banco_horas BOOLEAN DEFAULT FALSE;
ALTER TABLE contrato_estagio ADD COLUMN duracao_meses INT;
