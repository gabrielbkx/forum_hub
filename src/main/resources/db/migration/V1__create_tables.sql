CREATE TABLE cursos (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        nome VARCHAR(255) NOT NULL,
                        categoria VARCHAR(255) NOT NULL
);

CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nome VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL
);

CREATE TABLE perfis (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        nome VARCHAR(255) NOT NULL
);

CREATE TABLE usuario_perfil (
                                usuario_id BIGINT NOT NULL,
                                perfil_id BIGINT NOT NULL,
                                PRIMARY KEY (usuario_id, perfil_id),
                                FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                                FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);

CREATE TABLE topicos (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         titulo VARCHAR(255) NOT NULL,
                         mensagem TEXT NOT NULL,
                         data_criacao DATETIME,
                         status VARCHAR(50) NOT NULL,
                         autor_id BIGINT NOT NULL,
                         curso_id BIGINT NOT NULL,
                         FOREIGN KEY (autor_id) REFERENCES usuarios(id),
                         FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

CREATE TABLE respostas (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           mensagem TEXT NOT NULL,
                           data_criacao DATETIME,
                           solucao BOOLEAN,
                           topico_id BIGINT NOT NULL,
                           autor_id BIGINT NOT NULL,
                           FOREIGN KEY (topico_id) REFERENCES topicos(id),
                           FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
