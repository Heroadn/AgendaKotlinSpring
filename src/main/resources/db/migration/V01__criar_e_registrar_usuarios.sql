CREATE TABLE `usuario` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `senha` varchar(128) DEFAULT NULL,
  `celular` varchar(13) DEFAULT NULL,
  `foto` varchar(128) DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT NULL
);

INSERT INTO usuario (nome,email,senha,celular,foto,ativo) VALUES ('Administrador','admin@email.com','$2a$10$3/G88lXwKC5RFnPyfPtukOJ2hjv5VpYrupFSA09.sxJCwNLOJIlqS','481234124235','uploads/',1);
INSERT INTO usuario (nome,email,senha,celular,foto,ativo) VALUES ('Joao','joao@email.com','$2a$10$3/G88lXwKC5RFnPyfPtukOJ2hjv5VpYrupFSA09.sxJCwNLOJIlqS','489876124235','uploads/',1);
INSERT INTO usuario (nome,email,senha,celular,foto,ativo) VALUES ('Maria','maria@email.com','$2a$10$3/G88lXwKC5RFnPyfPtukOJ2hjv5VpYrupFSA09.sxJCwNLOJIlqS','484534124235','uploads/',0);
INSERT INTO usuario (nome,email,senha,celular,foto,ativo) VALUES ('Rose','rose@email.com','$2a$10$3/G88lXwKC5RFnPyfPtukOJ2hjv5VpYrupFSA09.sxJCwNLOJIlqS','481028124235','uploads/',0);