CREATE TABLE `cliente` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `senha` varchar(128) DEFAULT NULL,
  `celular` varchar(13) DEFAULT NULL,
  `foto` varchar(128) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
   FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION
);

INSERT INTO cliente (nome,email,senha,celular,foto,usuario_id) VALUES('Émile Durkheim','Durkheim@instituição.com','$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.','484356543456','/uploads',1);
INSERT INTO cliente (nome,email,senha,celular,foto,usuario_id) VALUES('Marx','marx@Comunistinha.com','$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.','487356543456','/uploads',2);

INSERT INTO cliente (nome,email,senha,celular,foto,usuario_id) VALUES('Leo','max@email.com','$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.','487356543456','/uploads',3);
INSERT INTO cliente (nome,email,senha,celular,foto,usuario_id) VALUES('Sarte','sarte@Existencialismo.com','$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.','489956543456','/uploads',4);