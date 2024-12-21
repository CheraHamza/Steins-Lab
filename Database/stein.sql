-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2024 at 02:45 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stein`
--

-- --------------------------------------------------------

--
-- Table structure for table `achats`
--

CREATE TABLE `achats` (
  `id` int(255) NOT NULL,
  `productName` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `montant` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `achats`
--

INSERT INTO `achats` (`id`, `productName`, `type`, `montant`, `date`) VALUES
(5, 'Test Tube', 'consomable', 50000, '2024-05-26'),
(6, 'Petri Dish', 'consomable', 20000, '2024-05-24');

-- --------------------------------------------------------

--
-- Table structure for table `analyses`
--

CREATE TABLE `analyses` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `nom_court` varchar(255) NOT NULL,
  `prix` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `analyses`
--

INSERT INTO `analyses` (`id`, `nom`, `nom_court`, `prix`) VALUES
(1, ' Complete Blood Count', ' CBC', 1500),
(2, ' Blood Glucose', ' BG', 1200),
(3, ' Lipid Profile', ' Lipid', 2500),
(4, ' Liver Function Test', ' LFT', 2000),
(5, ' Renal Function Test', ' RFT', 1800),
(6, ' Thyroid Function Test', ' TFT', 2200),
(7, ' Urea and Electrolytes', ' U&E', 1700),
(8, ' Hemoglobin A1c', ' HbA1c', 1600),
(9, ' Prostate-Specific Antigen', ' PSA', 3000),
(10, ' Viral Hepatitis Panel', ' VHP', 3500),
(11, ' HIV Test', ' HIV', 2000),
(12, ' Urinalysis', ' UA', 1000),
(13, ' Blood Culture', ' BC', 1500),
(14, ' Stool Culture', ' SC', 1300),
(15, ' Pap Smear', ' Pap', 1800),
(16, ' Allergy Test', ' Allergy', 5000);

-- --------------------------------------------------------

--
-- Table structure for table `bilan`
--

CREATE TABLE `bilan` (
  `id` int(11) NOT NULL,
  `id_client` int(11) DEFAULT NULL,
  `id_facture` varchar(255) DEFAULT NULL,
  `id_resultas` int(11) DEFAULT NULL,
  `date_com` date DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bilan`
--

INSERT INTO `bilan` (`id`, `id_client`, `id_facture`, `id_resultas`, `date_com`, `code`) VALUES
(15, 2, '43621323', 96030650, '2024-05-26', '89725009'),
(16, 3, '00364339', 22469707, '2024-05-26', '33484306'),
(17, 9, '23270492', NULL, '2024-05-26', '19012080'),
(18, 10, '63049655', NULL, '2024-05-26', '08735239'),
(19, 1, '26309052', NULL, '2024-05-27', '96274418'),
(20, 1, '43076343', NULL, '2024-05-28', '26309645'),
(21, 1, '80096205', NULL, '2024-05-28', '47548241');

-- --------------------------------------------------------

--
-- Table structure for table `bilan_analyses`
--

CREATE TABLE `bilan_analyses` (
  `id_bilan` int(11) DEFAULT NULL,
  `id_analyse` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bilan_analyses`
--

INSERT INTO `bilan_analyses` (`id_bilan`, `id_analyse`) VALUES
(15, 2),
(15, 3),
(16, 8),
(16, 9),
(17, 11),
(17, 12),
(18, 8),
(18, 13),
(18, 14),
(19, 4),
(19, 7),
(20, 1),
(20, 3),
(21, 1),
(21, 4);

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `date_nais` date DEFAULT NULL,
  `sexe` varchar(255) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `ville` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`id`, `nom`, `prenom`, `date_nais`, `sexe`, `telephone`, `email`, `ville`) VALUES
(1, 'Durand', 'Jean', '1980-05-15', 'Homme', '0123456789', 'jean.durand@example.com', 'Paris'),
(2, 'Martin', 'Sophie', '1992-11-23', 'Femme', '0987654321', 'sophie.martin@example.com', 'Lyon'),
(3, 'Bernard', 'Luc', '1975-02-10', 'Homme', '0223344556', 'luc.bernard@example.com', 'Marseille'),
(4, 'Thomas', 'Julie', '1988-07-07', 'Femme', '0336677889', 'julie.thomas@example.com', 'Toulouse'),
(5, 'Robert', 'Paul', '1983-03-22', 'Homme', '0445566778', 'paul.robert@example.com', 'Nice'),
(6, 'Petit', 'Claire', '1995-12-19', 'Femme', '0556677889', 'claire.petit@example.com', 'Nantes'),
(7, 'Rouge', 'Marc', '1979-08-30', 'Homme', '0667788990', 'marc.rouge@example.com', 'Strasbourg'),
(8, 'Moreau', 'Anne', '1986-01-17', 'Femme', '0778899001', 'anne.moreau@example.com', 'Montpellier'),
(9, 'Fabre', 'Eric', '1991-06-09', 'Homme', '0889900112', 'eric.fabre@example.com', 'Bordeaux'),
(10, 'Leroy', 'Laura', '1993-04-25', 'Femme', '0990011223', 'laura.leroy@example.com', 'Lille');

-- --------------------------------------------------------

--
-- Table structure for table `factures`
--

CREATE TABLE `factures` (
  `id` varchar(255) NOT NULL,
  `montant` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `factures`
--

INSERT INTO `factures` (`id`, `montant`) VALUES
('00364339', 4600),
('23270492', 3000),
('26309052', 3700),
('43076343', 4000),
('43621323', 3700),
('63049655', 4400),
('80096205', 3500);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `id` int(11) NOT NULL,
  `id_user` int(50) DEFAULT NULL,
  `montant` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`id`, `id_user`, `montant`, `date`) VALUES
(9, 4, 40000, '2024-05-01'),
(10, 3, 80000, '2024-05-01'),
(11, 2, 150000, '2024-05-01'),
(12, 1, 200000, '2024-05-01');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `productName`, `quantity`, `type`) VALUES
(1, 'Test Tube', 100, 'consomable'),
(2, 'Pipette', 50, 'consomable'),
(3, 'Centrifuge', 5, 'consomable'),
(4, 'Microscope', 10, 'consomable'),
(5, 'Reagent', 200, 'reactif'),
(6, 'Petri Dish', 150, 'consomable'),
(7, 'Burette', 30, 'consomable'),
(8, 'Incubator', 3, 'consomable'),
(9, 'Slides', 500, 'consomable'),
(10, 'pH Meter', 8, 'consomable');

-- --------------------------------------------------------

--
-- Table structure for table `resultat`
--

CREATE TABLE `resultat` (
  `id` int(11) NOT NULL,
  `resultats` varchar(255) DEFAULT NULL,
  `remarque` varchar(255) DEFAULT NULL,
  `etat` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resultat`
--

INSERT INTO `resultat` (`id`, `resultats`, `remarque`, `etat`) VALUES
(1, NULL, NULL, 0),
(22469707, '30ul,20ul', 'resulatat correcte', 1),
(96030650, '30ul,55.5ul', 'resultat dans les normes', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nom`, `prenom`, `username`, `password`, `type`) VALUES
(1, 'chera', 'hamza', 'hamzaChera', 'hisoka44', 'gerant'),
(2, 'Menad', 'Zakaria', 'zakariamenad', 'password2', 'doctor'),
(3, 'Chettab', 'Mohcin', 'mohcinchettab', 'password3', 'biologiste'),
(4, 'khaldi', 'mamoune', 'mamouneKhaldi', 'haha4', 'agent');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `achats`
--
ALTER TABLE `achats`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `analyses`
--
ALTER TABLE `analyses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bilan`
--
ALTER TABLE `bilan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_client` (`id_client`),
  ADD KEY `id_facture` (`id_facture`),
  ADD KEY `id_resultas` (`id_resultas`);

--
-- Indexes for table `bilan_analyses`
--
ALTER TABLE `bilan_analyses`
  ADD KEY `fk_tu_bi` (`id_bilan`),
  ADD KEY `fk_tu_an` (`id_analyse`);

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `factures`
--
ALTER TABLE `factures`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `resultat`
--
ALTER TABLE `resultat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `achats`
--
ALTER TABLE `achats`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `analyses`
--
ALTER TABLE `analyses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `bilan`
--
ALTER TABLE `bilan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bilan`
--
ALTER TABLE `bilan`
  ADD CONSTRAINT `bilan_ibfk_1` FOREIGN KEY (`id_client`) REFERENCES `clients` (`id`),
  ADD CONSTRAINT `bilan_ibfk_2` FOREIGN KEY (`id_facture`) REFERENCES `factures` (`id`),
  ADD CONSTRAINT `bilan_ibfk_3` FOREIGN KEY (`id_resultas`) REFERENCES `resultat` (`id`);

--
-- Constraints for table `bilan_analyses`
--
ALTER TABLE `bilan_analyses`
  ADD CONSTRAINT `fk_tu_an` FOREIGN KEY (`id_analyse`) REFERENCES `analyses` (`id`),
  ADD CONSTRAINT `fk_tu_bi` FOREIGN KEY (`id_bilan`) REFERENCES `bilan` (`id`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
