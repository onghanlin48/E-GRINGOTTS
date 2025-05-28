-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 09, 2024 at 11:33 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e-gringotts`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `userId` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `salt` varbinary(16) NOT NULL,
  `password` varbinary(256) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `address` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `category` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`userId`, `username`, `full_name`, `salt`, `password`, `email`, `phone`, `dob`, `address`, `status`, `category`) VALUES
(1, 'admin', 'admin', 0x0d8ac4aa17ad7fcd9462ebd6d84b5d74, 0x859821aa0c6d5054e565f74eb7bac2f0b931a59d05efa5fe62ecc134169fdf32, 'linwu212@gmail.com', 'admin', '2024-05-22', 'admin', 2, 4),
(17, 'Big Deeck', 'LIM JIAN CHUEN', 0xb5cb13dcfd79de79434aaf0a91c684ca, 0xcae70ebd6b5188e5e7ae0386a1e61862570e7e7184bc4944b6a35e3a2ed015ca, 'poseidonapollo11@gmail.com', '0123456789', '1890-01-08', 'KINABALU RESIDENTIAL COLLEGE', 2, 5),
(18, 'MyBoy', 'Wong Hoong Liang', 0xef8032fb05a1b3a32645ed4696cc4815, 0xf37cc2c9f989501d10c00e50a4b9b377bc4c69568284d3a542c76469c6242b9f, 'hoongliang03@gmail.com', '01159996969', '2018-01-09', 'KINABALU RESIDENTIAL COLLEGE', 2, 3),
(22, 'um', 'University Malaya', 0x18b4083a078039e9b2cc87dcc655b1ae, 0xd36c376103ca1f98f2263b41d0e67b3cb0ca8fd74d5faff25bec8650b7d966be, '23004928@siswa.um.edu.my', '01234567890', '2024-06-01', 'University Malaya', 2, 3),
(24, 'lincry', 'ONG HAN LIN', 0x4b4b23da93a250a1d56fd237aeede61f, 0x2ec4dcbcc74b7d63e4b53c22162be954a3dd46db6048bcc5eb6cc6ee809f8186, 'onghanlin48@gmail.com', '0132738352', '2001-02-12', 'lot 182', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `currency` int(11) NOT NULL,
  `balance` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`currency`, `balance`) VALUES
(1, 20),
(2, 30),
(3, 12);

-- --------------------------------------------------------

--
-- Table structure for table `amount`
--

CREATE TABLE `amount` (
  `id` int(255) NOT NULL,
  `cid` int(11) NOT NULL,
  `amount` double NOT NULL,
  `userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `amount`
--

INSERT INTO `amount` (`id`, `cid`, `amount`, `userid`) VALUES
(17, 1, 80, 17),
(18, 2, 1975.89, 17),
(19, 3, 0, 17),
(20, 1, 0, 18),
(21, 2, 0, 18),
(22, 3, 0, 18),
(32, 1, 110, 22),
(33, 2, 0, 22),
(34, 3, 0, 22),
(42, 1, 44.49, 24),
(43, 2, 0, 24),
(44, 3, 0, 24);

-- --------------------------------------------------------

--
-- Table structure for table `card`
--

CREATE TABLE `card` (
  `number` varchar(255) NOT NULL,
  `mmyy` varchar(255) NOT NULL,
  `cvv` varchar(255) NOT NULL,
  `salt` varbinary(16) DEFAULT NULL,
  `PIN` varbinary(256) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `try` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `card`
--

INSERT INTO `card` (`number`, `mmyy`, `cvv`, `salt`, `PIN`, `id`, `try`) VALUES
('40972067163818536', '629', '635', 0xcf3330f8be5e42d37519a8e45be9b55c, 0xcb473183591763884dfeef084484b63f9c6f68f57284210420b62682045e3be1, 24, 0),
('49995887734634572', '629', '670', 0x7473534930f82a35aef57f895028102d, 0x21ef5666eefac72a399ac12f63197df25196c29fa1b0122f70bfb5fef6c0eed6, 17, 0);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `discount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `discount`) VALUES
(1, 'Golden Galleon', 0),
(2, 'Silver Snitch', 10),
(3, 'Platinum Patronus', 20),
(4, 'admin', 100),
(5, 'no member', 0);

-- --------------------------------------------------------

--
-- Table structure for table `currency`
--

CREATE TABLE `currency` (
  `id` int(11) NOT NULL,
  `currency` varchar(11) NOT NULL,
  `short` varchar(255) NOT NULL,
  `v` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `currency`
--

INSERT INTO `currency` (`id`, `currency`, `short`, `v`) VALUES
(1, 'Knut', 'K', 14297),
(2, 'Sickle', 'S', 414613),
(3, 'Galleon', 'G', 7048421);

-- --------------------------------------------------------

--
-- Table structure for table `favourite`
--

CREATE TABLE `favourite` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `favourite` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `favourite`
--

INSERT INTO `favourite` (`id`, `user_id`, `favourite`) VALUES
(53, 18, 17),
(54, 24, 18),
(55, 17, 24);

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `invoice` varchar(255) NOT NULL,
  `form` int(11) NOT NULL,
  `receive` int(11) NOT NULL,
  `amount` double NOT NULL,
  `currency` int(11) NOT NULL,
  `date` date NOT NULL,
  `category` varchar(255) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `status` varchar(11) NOT NULL,
  `user` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `history`
--

INSERT INTO `history` (`id`, `invoice`, `form`, `receive`, `amount`, `currency`, `date`, `category`, `reference`, `status`, `user`) VALUES
(230, 'INV-20240608203530-9435', 24, 17, 44.49, 1, '2024-06-08', 'Quick Transfer', 'loan', '1', 'Online'),
(231, 'INV-20240608203530-9435', 17, 17, 1000, 2, '2024-06-08', 'Convert Currency', 'Convert Currency', '2', 'Online'),
(232, 'INV-20240608203530-9435', 17, 1, 30, 2, '2024-06-08', 'Fee', 'Processing Fee', '2', 'Online'),
(233, 'INV-20240608203530-9435', 17, 17, 34.49, 1, '2024-06-08', 'Convert Currency', 'Convert Currency', '1', 'Online'),
(234, 'INV-20240608203530-9435', 17, 24, 44.49, 1, '2024-06-08', 'Quick Transfer', 'loan', '2', 'Online'),
(235, 'INV-20240608203757-9388', 17, 22, 100, 1, '2024-06-08', 'Food', 'Food', '2', 'Debit Card'),
(236, 'INV-20240608203757-9388', 22, 17, 100, 1, '2024-06-08', 'Food', 'Food', '1', 'Debit Card'),
(237, 'INV-20240608204128-6864', 17, 22, 100, 1, '2024-06-08', 'Food', 'Food', '2', 'Debit Card'),
(238, 'INV-20240608204128-6864', 22, 17, 100, 1, '2024-06-08', 'Food', 'Food', '1', 'Debit Card'),
(239, 'INV-20240608211504-4827', 17, 22, 10, 1, '2024-06-08', 'Food', 'Food', '2', 'Debit Card'),
(240, 'INV-20240608211504-4827', 22, 17, 10, 1, '2024-06-08', 'Food', 'Food', '1', 'Debit Card');

-- --------------------------------------------------------

--
-- Table structure for table `image`
--

CREATE TABLE `image` (
  `id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `image`
--

INSERT INTO `image` (`id`, `path`) VALUES
(5, 'Group 12.png'),
(6, 'Group 13.png'),
(7, 'Group 14.png'),
(10, 'Group 17.png');

-- --------------------------------------------------------

--
-- Table structure for table `proccessing`
--

CREATE TABLE `proccessing` (
  `id` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `fee` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `proccessing`
--

INSERT INTO `proccessing` (`id`, `cid`, `c_id`, `value`, `fee`) VALUES
(1, 1, 3, 493, 2),
(2, 1, 2, 29, 20),
(3, 2, 3, 17, 8),
(4, 2, 1, 0.034482758620689655, 3),
(5, 3, 1, 0.002028397565922921, 5),
(6, 3, 2, 0.05882, 12);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `phone` (`phone`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `category` (`category`);

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`currency`);

--
-- Indexes for table `amount`
--
ALTER TABLE `amount`
  ADD PRIMARY KEY (`id`),
  ADD KEY `currency` (`cid`),
  ADD KEY `user_id` (`userid`);

--
-- Indexes for table `card`
--
ALTER TABLE `card`
  ADD PRIMARY KEY (`number`),
  ADD KEY `id_c` (`id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `currency`
--
ALTER TABLE `currency`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `short` (`short`);

--
-- Indexes for table `favourite`
--
ALTER TABLE `favourite`
  ADD PRIMARY KEY (`id`),
  ADD KEY `from` (`user_id`),
  ADD KEY `fa` (`favourite`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `currency` (`currency`);

--
-- Indexes for table `image`
--
ALTER TABLE `image`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `proccessing`
--
ALTER TABLE `proccessing`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cid` (`cid`),
  ADD KEY `c_id` (`c_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `amount`
--
ALTER TABLE `amount`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `currency`
--
ALTER TABLE `currency`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `favourite`
--
ALTER TABLE `favourite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=241;

--
-- AUTO_INCREMENT for table `image`
--
ALTER TABLE `image`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `proccessing`
--
ALTER TABLE `proccessing`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `category` FOREIGN KEY (`category`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`currency`) REFERENCES `currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `amount`
--
ALTER TABLE `amount`
  ADD CONSTRAINT `currency` FOREIGN KEY (`cid`) REFERENCES `currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `user_id` FOREIGN KEY (`userid`) REFERENCES `account` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `card`
--
ALTER TABLE `card`
  ADD CONSTRAINT `id_c` FOREIGN KEY (`id`) REFERENCES `account` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `favourite`
--
ALTER TABLE `favourite`
  ADD CONSTRAINT `fa` FOREIGN KEY (`favourite`) REFERENCES `account` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `from` FOREIGN KEY (`user_id`) REFERENCES `account` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`currency`) REFERENCES `currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `proccessing`
--
ALTER TABLE `proccessing`
  ADD CONSTRAINT `proccessing_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proccessing_ibfk_2` FOREIGN KEY (`c_id`) REFERENCES `currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
