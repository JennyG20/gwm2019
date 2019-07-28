CREATE TABLE `main`.`user` (
  `id` INT NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `secondname` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `usertype` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

CREATE TABLE `main`.`assessment` (
  `id` INT NOT NULL,
  `supervisor` INT NOT NULL,
  `group` INT NOT NULL,
  `desc` VARCHAR(500) NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `created` VARCHAR(45) NOT NULL,
  `deadline` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `id`
    FOREIGN KEY (`id`)
    REFERENCES `main`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `main`.`part_assessment` (
  `id` INT NOT NULL,
  `user` INT NOT NULL,
  `assessment` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `student_idx` (`user` ASC) VISIBLE,
  INDEX `assessment_idx` (`assessment` ASC) VISIBLE,
  CONSTRAINT `user3`
    FOREIGN KEY (`user`)
    REFERENCES `main`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `assessment`
    FOREIGN KEY (`assessment`)
    REFERENCES `main`.`assessment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `main`.`group` (
  `id` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `main`.`part_group` (
  `id` INT NOT NULL,
  `group` INT NOT NULL,
  `user` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `group_idx` (`group` ASC) VISIBLE,
  INDEX `user_idx` (`user` ASC) VISIBLE,
  CONSTRAINT `group`
    FOREIGN KEY (`group`)
    REFERENCES `main`.`group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user`
    FOREIGN KEY (`user`)
    REFERENCES `main`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `main`.`task` (
  `id` INT NOT NULL,
  `description` VARCHAR(200) NOT NULL,
  `deadline` VARCHAR(45) NOT NULL,
  `assessment` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `assessment_idx` (`assessment` ASC) VISIBLE,
  CONSTRAINT `assessment2`
    FOREIGN KEY (`assessment`)
    REFERENCES `main`.`assessment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `main`.`part_task` (
  `id` INT NOT NULL,
  `user` INT NOT NULL,
  `task` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_idx` (`user` ASC) VISIBLE,
  INDEX `task_idx` (`task` ASC) VISIBLE,
  CONSTRAINT `user2`
    FOREIGN KEY (`user`)
    REFERENCES `main`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `task`
    FOREIGN KEY (`task`)
    REFERENCES `main`.`task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
