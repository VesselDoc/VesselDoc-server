
CREATE SCHEMA IF NOT EXISTS `vesseldoc` DEFAULT CHARACTER SET utf8 ;
USE `vesseldoc` ;

-- -----------------------------------------------------
-- Table `vesseldoc`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vesseldoc`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(61) NOT NULL,
  `role_id` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_form_role_idx` (`role_id` ASC) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) ,
  CONSTRAINT `fk_form_role`
      FOREIGN KEY (`role_id`)
          REFERENCES `vesseldoc`.`role` (`id`)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `vesseldoc`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vesseldoc`.`role` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(64) NOT NULL,
	PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vesseldoc`.`form_structure`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vesseldoc`.`form_structure` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `content` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vesseldoc`.`form`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vesseldoc`.`form` (
  `id` BINARY(16) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `form_structure_id` INT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_form_user_idx` (`user_id` ASC) ,
  INDEX `fk_form_form_structure1_idx` (`form_structure_id` ASC) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `fk_form_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `vesseldoc`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_form_form_structure1`
    FOREIGN KEY (`form_structure_id`)
    REFERENCES `vesseldoc`.`form_structure` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

