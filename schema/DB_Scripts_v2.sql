DROP DATABASE phones;
CREATE DATABASE phones;
USE phones;

SET @@global.time_zone = '+00:00';
SET @@session.time_zone = '+00:00';


/* TABLES */
CREATE TABLE `provinces` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT `Pk_provinces` PRIMARY KEY (`id`)
);


CREATE TABLE `cities` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `prefix` VARCHAR(255) NOT NULL UNIQUE,
    `id_province` INT NOT NULL,
    CONSTRAINT `Pk_cities` PRIMARY KEY (`id`),
	CONSTRAINT `Fk_cities_province` FOREIGN KEY(`id_province`) REFERENCES `provinces`(`id`)
);


CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `surname` VARCHAR(255) NOT NULL,
    `dni` VARCHAR(255) NOT NULL UNIQUE,
	`username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,

    /* Si hay error, usar DATETIME en vez de TIMESTAMP */
    `creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    `is_active` BOOLEAN DEFAULT TRUE,
    `id_city` INT NOT NULL,    
    CONSTRAINT `Pk_users` PRIMARY KEY (`id`),
    CONSTRAINT `Fk_users_city` FOREIGN KEY(`id_city`) REFERENCES `cities`(`id`)    
);


CREATE TABLE `user_roles` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(255) NOT NULL,    
    CONSTRAINT `Pk_user_roles` PRIMARY KEY (`id`)
);


CREATE TABLE `users_x_user_roles` (
	`id_user` INT NOT NULL,
    `id_user_role` INT NOT NULL,
    CONSTRAINT `Pk_users_x_user_roles` PRIMARY KEY (`id_user`, `id_user_role`),
    CONSTRAINT `Fk_users_x_user_roles__user` FOREIGN KEY(`id_user`) REFERENCES `users`(`id`),
    CONSTRAINT `Fk_users_x_user_roles__user_rol` FOREIGN KEY(`id_user_role`) REFERENCES `user_roles`(`id`)
);


CREATE TABLE `line_types` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `type` VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT `Pk_line_types` PRIMARY KEY (`id`)    
);


CREATE TABLE `lines` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `number` VARCHAR(255) NOT NULL UNIQUE,

    `creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    `status` ENUM('ENABLED', 'DISABLED', 'SUSPENDED') NOT NULL,
    `id_user` INT NOT NULL,
    `id_line_type` INT NOT NULL,
	CONSTRAINT `Pk_lines` PRIMARY KEY (`id`),
    CONSTRAINT `Fk_lines_user` FOREIGN KEY(`id_user`) REFERENCES `users`(`id`),
    CONSTRAINT `Fk_lines_line_type` FOREIGN KEY(`id_line_type`) REFERENCES `line_types`(`id`)
);


CREATE TABLE `rates` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `price_minute` FLOAT NOT NULL,
    `cost` FLOAT NOT NULL,
    `id_city_origin` INT NOT NULL,
    `id_city_destination` INT NOT NULL,
    CONSTRAINT `Pk_rates` PRIMARY KEY (`id`),
    CONSTRAINT `Fk_rates_city_origin` FOREIGN KEY(`id_city_origin`) REFERENCES `cities`(`id`),
    CONSTRAINT `Fk_rates_city_destination` FOREIGN KEY(`id_city_destination`) REFERENCES `cities`(`id`),
    CONSTRAINT `Unique_rates` UNIQUE (`id_city_origin`, `id_city_destination`)
);


CREATE TABLE `invoices` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `number_calls` INT DEFAULT 0,
    `cost_price` FLOAT NOT NULL,
    `total_price` FLOAT NOT NULL,
    `id_line` INT NOT NULL,

    `creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    `due_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `is_paid` BOOLEAN DEFAULT FALSE,
    CONSTRAINT `Pk_invoices` PRIMARY KEY (`id`),
    CONSTRAINT `Fk_invoices_line` FOREIGN KEY(`id_line`) REFERENCES `lines`(`id`)
);


CREATE TABLE `calls` (
	`id` INT AUTO_INCREMENT NOT NULL,
    `duration` INT,
    `total_price` FLOAT,
    `creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `id_origin_line` INT,
    `id_destination_line` INT,
    `id_rate` INT,
    `id_invoice` INT,
    `origin_number` VARCHAR(255),
    `destination_number` VARCHAR(255),
    CONSTRAINT `Pk_calls` PRIMARY KEY (`id`),
    CONSTRAINT `Fk_calls_origin_line` FOREIGN KEY(`id_origin_line`) REFERENCES `lines`(`id`),
    CONSTRAINT `Fk_calls_destination_line` FOREIGN KEY(`id_destination_line`) REFERENCES `lines`(`id`),
    CONSTRAINT `Fk_calls_rate` FOREIGN KEY(`id_rate`) REFERENCES `rates`(`id`),
    CONSTRAINT `Fk_calls_invoice` FOREIGN KEY(`id_invoice`) REFERENCES `invoices`(`id`)
);


/* BASIC DATA */

	/* Provinces */
    insert into `provinces`(name) values ('Buenos Aires');


    /* Cities */
    insert into 
		`cities`
			(name, prefix, id_province) 
		values 
			('Mar del Plata', '223', 1),
            ('Miramar', '2291', 1),
            ('La Plata', '221', 1),
            ('Buenos Aires', '11', 1),
            ('Bahia Blanca', '291', 1),
            ('Azul', '2281', 1),
            ('Maipu', '2268', 1);


	/* Rates */
    insert into
		`rates`
			(price_minute, cost, id_city_origin, id_city_destination)
		values
			(10, 5, 1, 1),		/* Mdp to Mdp */
			(12, 5, 1, 2),		/* Mdp to Miramar */
            (20, 5, 1, 3),		/* Mdp to La Plata */
            (22, 5, 1, 4),		/* Mdp to Buenos Aires */
            (18, 5, 1, 5),		/* Mdp to Bahia Blanca */
            (15, 5, 1, 6),		/* Mdp to Azul */
            (14, 5, 1, 7);		/* Mdp to Maipu */


	/* User Role */
    insert into
		`user_roles`
			(role)
		values
			('Employee'),
            ('Client');


	/* User */
	insert into
		`users`
			(name, surname, dni, username, password, creation_date, is_active, id_city)
		values
			('Rodrigo', 'Leon', '404040', 'Rl97', '$2a$04$a3gVk/wqNx9hvoFRjyr5aefKMVpo.23HSOlxePz4pqKLiQAppm4de', now(), 1, 1);


	/* Users x User_Roles */
    insert into
		`users_x_user_roles`
			(id_user, id_user_role)
		values
			(1, 1),
            (1, 2);


	/* Line Type */
    insert into
		`line_types`
			(type)
			values
				('Mobile'),
                ('Residential');


	/* Line */
        insert into
			`lines`
				(number, creation_date, status, id_user, id_line_type)
			values				
				('2235245050', now(), 'ENABLED',1, 1);



/* FUNCTIONS */
DELIMITER $$
CREATE FUNCTION `find_city_by_call_number`(number VARCHAR(255)) RETURNS INT DETERMINISTIC
BEGIN
	DECLARE id INT DEFAULT -1;
    SELECT c.id INTO id FROM cities c WHERE number LIKE CONCAT(c.prefix, '%') ORDER BY length(c.prefix) DESC LIMIT 1;
    RETURN id;
END $$


/* TRIGGERS */
DROP TRIGGER IF EXISTS `tbi_calls_new`;
DELIMITER $$
CREATE TRIGGER `tbi_calls_new` BEFORE INSERT ON `calls`
	FOR EACH ROW BEGIN

        DECLARE idOriginLine INT DEFAULT -1;
        DECLARE idDestinationLine INT DEFAULT -1;
        DECLARE idOriginCity INT DEFAULT -1;
        DECLARE idDestinationCity INT DEFAULT -1;
        DECLARE idRate INT DEFAULT -1;
        DECLARE priceRate INT DEFAULT -1;

        SELECT l.id INTO idOriginLine FROM `lines` l WHERE l.number = NEW.origin_number;
        SELECT l.id INTO idDestinationLine FROM `lines` l WHERE l.number = NEW.destination_number;

        SET idOriginCity = find_city_by_call_number(NEW.origin_number);
        SET idDestinationCity = find_city_by_call_number(NEW.destination_number);

        IF (idOriginCity > 0 AND idDestinationCity > 0) THEN

            SET NEW.id_origin_line = idOriginLine;
            SET NEW.id_destination_line = idDestinationLine;

			SELECT r.id, r.price_minute INTO idRate, priceRate FROM `rates` r WHERE r.id_city_origin = idOriginCity AND r.id_city_destination = idDestinationCity;

            IF (idRate > 0) THEN
                SET NEW.total_price = ((NEW.duration / 60) * priceRate);
                SET NEW.id_rate = idRate;
			ELSE
				SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Could not be found rate for calls';
            END IF;

        ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'The numbers are invalid';
        END IF;
END $$


/* FACTURACION */
- recorrer todas las llamadas que no esten en una factura
- determinar linea
- cantidad de llamadas no facturadas
- precio total
- fecha de creacion
- fecha de vencimiento (+15 dias fecha de creacion)
- generar una facturar por cada linea
- updatear las llamadas, y ponerle la factura correspondiente

-- Procedimiento - NO ANDA ESTE
DROP PROCEDURE `sp_invoice_create`;
DELIMITER $$
CREATE PROCEDURE `sp_invoice_create`()
BEGIN
    DECLARE vTotalPrice FLOAT DEFAULT 0;
    DECLARE vCostPrice FLOAT DEFAULT 0;
    DECLARE vIdCall INT DEFAULT -1;
    DECLARE vIdLine INT DEFAULT -1;
    DECLARE vIdInvoice INT DEFAULT -1;
    DECLARE vNumberCalls INT DEFAULT 0;
    DECLARE vFinished INT DEFAULT 0;

    #Obtenemos todas las llamadas no facturadas, con su linea, cantidad de llamadas y precio total
    DECLARE cur_calls_invoice CURSOR FOR
        SELECT
            c.id_origin_line,
            count(c.id) AS number_calls,
            sum(c.total_price) AS total_price
        FROM `calls` c
        WHERE c.id_invoice IS NULL
        GROUP BY c.id_origin_line;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    #Empezamos a recorrer, fila por fila
    OPEN cur_calls_invoice;
    FETCH cur_calls_invoice INTO vIdLine, vNumberCalls, vTotalPrice;
    WHILE (vFinished = 0) DO

        #Se inserta la factura
        INSERT INTO invoices(number_calls, cost_price, total_price, id_line, creation_date, due_date, is_paid)
        VALUES(vNumberCalls, 0, vTotalPrice, vIdLine, now(), now() + INTERVAL 15 DAY, 0);

        #Se toma el id de la factura
        SET vIdInvoice = last_insert_id();

        #Se updatea las llamadas asignandole la factura
        #Como saco el id de llamadas?
        #UPDATE `calls` SET id_invoice = vIdInvoice WHERE id = vIdCall;

        FETCH cur_calls_invoice INTO vIdLine, vNumberCalls, vTotalPrice;

    END while;
    CLOSE cur_calls_invoice;
END
$$



-- Secundary - works!
DELIMITER $$
CREATE PROCEDURE `sp_invoices_lines`()
BEGIN

    DECLARE vIdLine INT DEFAULT -1;
    DECLARE vFinished INT DEFAULT 0;

    DECLARE cur_lines_invoice CURSOR FOR SELECT l.id FROM `lines` l;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_lines_invoice;
    FETCH cur_lines_invoice INTO vIdLine;
    WHILE (vFinished = 0) DO
        CALL sp_li_lines(vIdLine);
        FETCH cur_lines_invoice INTO vIdLine;
    END while;
    CLOSE cur_lines_invoice;
END
$$

DROP PROCEDURE `sp_li_lines`;
DELIMITER $$
CREATE PROCEDURE `sp_li_lines`(IN vIdLine INT)
BEGIN

    DECLARE vTotalPrice FLOAT DEFAULT 0;
    DECLARE vCostPrice FLOAT DEFAULT 0;
    DECLARE vNumberCalls INTEGER DEFAULT 0;
    DECLARE vIdInvoice INTEGER;

    ##SET NEW.total_price = ((NEW.duration / 60) * priceRate);

    SELECT
            count(c.id_origin_line),
            ifnull(sum(c.total_price), 0),
            --ifnull(sum(select r.cost from calls c inner join rates r on r.id = c.id_rate), 0)
            ifnull( select r.cost as cost from calls c inner join rates r on r.id = c.id_rate where c.id_origin_line = 1, 0)
        INTO vNumberCalls, vTotalPrice
    FROM `calls` c
    INNER JOIN `lines` l ON c.id_origin_line = l.id
    WHERE c.id_invoice IS NULL AND l.id = vIdLine;

    INSERT INTO invoices(number_calls, cost_price, total_price, id_line, creation_date, due_date, is_paid)
    VALUES(vNumberCalls, vCostPrice, vTotalPrice, vIdLine, now(), now() + INTERVAL 15 DAY, 0);

    SET vIdInvoice = last_insert_id();

    UPDATE `calls`
    SET id_invoice = vIdInvoice
    WHERE id_origin_line = vIdLine AND id_invoice IS NULL;

END
$$

-- Event
SET GLOBAL event_scheduler = ON;
DROP EVENT IF EXISTS `event_invoice`;

DELIMITER $$
CREATE EVENT IF NOT EXISTS `event_invoice`
ON SCHEDULE EVERY '1' MONTH STARTS CURDATE() + INTERVAL 1 MONTH - INTERVAL (DAYOFMONTH(CURDATE()) - 1) DAY
DO
    BEGIN
        CALL sp_invoices_lines();
    END
END $$


/* USUARIOS */

 // BackOffice
 CREATE USER 'backoffice'@'localhost' identified BY '123';
 GRANT ALL ON phones.users TO 'backoffice'@'localhost';
 GRANT ALL ON phones.lines TO 'backoffice'@'localhost';
 GRANT ALL ON phones.rates TO 'backoffice'@'localhost';
 GRANT ALL ON phones.invoices TO 'backoffice'@'localhost';

 // Clients
 CREATE USER 'clients'@'localhost' identified BY '123';
 GRANT SELECT ON phones.calls TO 'clients'@'localhost';
 GRANT SELECT ON phones.invoices TO 'clients'@'localhost';
 GRANT SELECT ON phones.cities TO clients'@'localhost';

 // Infrastructure
 CREATE USER 'infrastructure'@'localhost' identified BY '123';
 GRANT INSERT ON phones.calls TO 'infrastructure'@'localhost';
 grant trigger on phones.* TO 'infrastructure'@'localhost';     // Q HACE

 // Invoice
 CREATE USER 'invoice'@'localhost' identified BY '1234';
 GRANT EVENT ON TPFinal.* TO 'invoice'@'localhost';
 GRANT EXECUTE ON PROCEDURE sp_get_calls_cost_and_price TO 'invoice'@'localhost';
 GRANT EXECUTE ON PROCEDURE sp_assign_id_invoice_to_calls TO 'invoice'@'localhost';


/* VISTAS */
-- USAR UNA VISTA PARA EL REPORTE???? (PUNTO 4)
    -- CONSULTA DE LLAMADAS POR USUARIO Y FECHA
          -- i) Número de origen
          -- ii) Ciudad de origen
          -- iii) Número de destino
          -- iv) Ciudad de destino
          -- v) Precio total
          -- vi) Duración
          -- vii) Fecha y hora de llamada



/* NOSQL */