CREATE TABLE `agendamento` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `data_registro` datetime DEFAULT NULL,
  `data_agendamento` datetime DEFAULT NULL,
  `status` varchar(9) DEFAULT NULL,
  `motivo` varchar(200) DEFAULT NULL,
  `usuario_id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
   FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
   FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
);

INSERT INTO agendamento (data_registro,data_agendamento,status,motivo,usuario_id,cliente_id)
VALUES('2018/12/13','2019/02/14','ABERTO','Discurso sobre as instituiçoes', 1, 1);

INSERT INTO agendamento (data_registro,data_agendamento,status,motivo,usuario_id,cliente_id)
VALUES('2018/12/13','2019/02/14','FECHADO','Discurso do papai noel', 2, 2);

INSERT INTO agendamento (data_registro,data_agendamento,status,motivo,usuario_id,cliente_id)
VALUES('2018/12/13','2019/02/14','ABERTO','Discurso sobre o comunismo', 3, 3);

INSERT INTO agendamento (data_registro,data_agendamento,status,motivo,usuario_id,cliente_id)
VALUES('2018/12/13','2019/02/14','CANCELADO','Discurso sobre capitalismo', 4, 4);

