-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 17, 2025 at 03:15 PM
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
-- Database: `joymarket`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `idAdmin` varchar(50) NOT NULL,
  `emergencyContact` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`idAdmin`, `emergencyContact`) VALUES
('AD001', '08199999999');

-- --------------------------------------------------------

--
-- Table structure for table `cartitem`
--

CREATE TABLE `cartitem` (
  `idCustomer` varchar(50) NOT NULL,
  `idProduct` varchar(50) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `courier`
--

CREATE TABLE `courier` (
  `idCourier` varchar(50) NOT NULL,
  `vehicleType` varchar(50) NOT NULL,
  `vehiclePlate` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courier`
--

INSERT INTO `courier` (`idCourier`, `vehicleType`, `vehiclePlate`) VALUES
('CR001', 'Motor', 'B 1234 ABC'),
('CR002', 'Mobil', 'D 5678 XYZ');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `idCustomer` varchar(50) NOT NULL,
  `balance` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`idCustomer`, `balance`) VALUES
('CU001', 10300000);

-- --------------------------------------------------------

--
-- Table structure for table `delivery`
--

CREATE TABLE `delivery` (
  `idOrder` varchar(50) NOT NULL,
  `idCourier` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `delivery`
--

INSERT INTO `delivery` (`idOrder`, `idCourier`, `status`) VALUES
('OR001', 'CR001', 'Delivered'),
('OR002', 'CR002', 'Delivered'),
('OR003', 'CR002', 'Delivered'),
('OR004', 'CR002', 'Delivered'),
('OR005', 'CR002', 'Delivered'),
('OR006', 'CR002', 'Processed');

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE `orderdetail` (
  `idOrder` varchar(50) NOT NULL,
  `idProduct` varchar(50) NOT NULL,
  `qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`idOrder`, `idProduct`, `qty`) VALUES
('OR001', 'PR001', 3),
('OR002', 'PR001', 5),
('OR003', 'PR005', 5),
('OR004', 'PR002', 5),
('OR005', 'PR002', 10),
('OR006', 'PR001', 5);

-- --------------------------------------------------------

--
-- Table structure for table `orderheader`
--

CREATE TABLE `orderheader` (
  `idOrder` varchar(50) NOT NULL,
  `idCustomer` varchar(50) NOT NULL,
  `idPromo` varchar(50) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `orderAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderheader`
--

INSERT INTO `orderheader` (`idOrder`, `idCustomer`, `idPromo`, `status`, `orderAt`) VALUES
('OR001', 'CU001', NULL, 'Delivered', '2025-12-15 21:27:22'),
('OR002', 'CU001', NULL, 'Delivered', '2025-12-16 20:42:15'),
('OR003', 'CU001', NULL, 'Delivered', '2025-12-16 21:01:02'),
('OR004', 'CU001', NULL, 'Delivered', '2025-12-16 21:28:56'),
('OR005', 'CU001', NULL, 'Delivered', '2025-12-16 22:31:56'),
('OR006', 'CU001', NULL, 'Processed', '2025-12-16 23:05:52');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `idProduct` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `stock` int(11) NOT NULL,
  `category` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`idProduct`, `name`, `price`, `stock`, `category`) VALUES
('PR001', 'Apple', 20000, 973, 'Fruit'),
('PR002', 'Banana', 2000, 999970, 'Fruit'),
('PR003', 'Pear', 10000, 20, 'Fruit'),
('PR004', 'Pineapple', 20000, 100, 'Fruits'),
('PR005', 'Watermelon', 2000, 5, 'Fruits'),
('PR006', 'Melon', 10000, 80, 'Fruits'),
('PR007', 'Orange', 15000, 120, 'Fruits'),
('PR008', 'Mango', 25000, 60, 'Fruits');

-- --------------------------------------------------------

--
-- Table structure for table `promo`
--

CREATE TABLE `promo` (
  `idPromo` varchar(50) NOT NULL,
  `code` varchar(20) NOT NULL,
  `headline` varchar(255) DEFAULT NULL,
  `discountPercentage` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `userbase`
--

CREATE TABLE `userbase` (
  `idUser` varchar(50) NOT NULL,
  `fullName` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userbase`
--

INSERT INTO `userbase` (`idUser`, `fullName`, `email`, `password`, `phone`, `address`) VALUES
('AD001', 'Admin Utama', 'admin@gmail.com', 'admin123', '08123456789', 'Jakarta'),
('CR001', 'Gosend', 'courier1@gmail.com', 'courier123', '08111111111', 'Jakarta'),
('CR002', 'Grabsend', 'courier2@gmail.com', 'courier123', '08222222222', 'Bandung'),
('CU001', 'yaya', 'yahaha@gmail.com', '123456', '08645382652', 'jl.budi');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`idAdmin`);

--
-- Indexes for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD PRIMARY KEY (`idCustomer`,`idProduct`),
  ADD KEY `idProduct` (`idProduct`);

--
-- Indexes for table `courier`
--
ALTER TABLE `courier`
  ADD PRIMARY KEY (`idCourier`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`idCustomer`);

--
-- Indexes for table `delivery`
--
ALTER TABLE `delivery`
  ADD PRIMARY KEY (`idOrder`),
  ADD KEY `idCourier` (`idCourier`);

--
-- Indexes for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD PRIMARY KEY (`idOrder`,`idProduct`),
  ADD KEY `idProduct` (`idProduct`);

--
-- Indexes for table `orderheader`
--
ALTER TABLE `orderheader`
  ADD PRIMARY KEY (`idOrder`),
  ADD KEY `idCustomer` (`idCustomer`),
  ADD KEY `idPromo` (`idPromo`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`idProduct`);

--
-- Indexes for table `promo`
--
ALTER TABLE `promo`
  ADD PRIMARY KEY (`idPromo`),
  ADD UNIQUE KEY `code` (`code`);

--
-- Indexes for table `userbase`
--
ALTER TABLE `userbase`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `userbase` (`idUser`);

--
-- Constraints for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`),
  ADD CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`);

--
-- Constraints for table `courier`
--
ALTER TABLE `courier`
  ADD CONSTRAINT `courier_ibfk_1` FOREIGN KEY (`idCourier`) REFERENCES `userbase` (`idUser`);

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `userbase` (`idUser`);

--
-- Constraints for table `delivery`
--
ALTER TABLE `delivery`
  ADD CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orderheader` (`idOrder`),
  ADD CONSTRAINT `delivery_ibfk_2` FOREIGN KEY (`idCourier`) REFERENCES `courier` (`idCourier`);

--
-- Constraints for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orderheader` (`idOrder`),
  ADD CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`);

--
-- Constraints for table `orderheader`
--
ALTER TABLE `orderheader`
  ADD CONSTRAINT `orderheader_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`),
  ADD CONSTRAINT `orderheader_ibfk_2` FOREIGN KEY (`idPromo`) REFERENCES `promo` (`idPromo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
