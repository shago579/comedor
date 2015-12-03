-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-11-2015 a las 21:54:12
-- Versión del servidor: 5.6.21
-- Versión de PHP: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `comedor`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comensales`
--

CREATE TABLE IF NOT EXISTS `comensales` (
  `CURP` varchar(20) NOT NULL DEFAULT '',
  `nombre` varchar(35) DEFAULT NULL,
  `apellidos` varchar(50) DEFAULT NULL,
  `permisos` int(11) DEFAULT NULL,
  `rango` int(11) DEFAULT NULL,
  `foto` longblob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comidas`
--

CREATE TABLE IF NOT EXISTS `comidas` (
  `idComida` int(11) NOT NULL,
  `comida` varchar(45) NOT NULL,
  `horaInicio` time NOT NULL,
  `horaFinal` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `comidas`
--

INSERT INTO `comidas` (`idComida`, `comida`, `horaInicio`, `horaFinal`) VALUES
(1, 'Desayuno', '07:00:00', '11:00:00'),
(2, 'Comida', '14:00:00', '18:00:00'),
(3, 'Cena', '18:00:00', '20:30:00'),
(4, 'Colacion', '22:00:00', '01:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos`
--

CREATE TABLE IF NOT EXISTS `permisos` (
`permisoId` int(11) NOT NULL,
  `permiso` varchar(40) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `permisos`
--

INSERT INTO `permisos` (`permisoId`, `permiso`) VALUES
(1, 'Todos'),
(2, 'Desayuno-Comida'),
(3, 'Colacion');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisoscomidas`
--

CREATE TABLE IF NOT EXISTS `permisoscomidas` (
  `idPermiso` int(11) NOT NULL,
  `idComida` int(11) NOT NULL,
  `idpermisoscomidas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `permisoscomidas`
--

INSERT INTO `permisoscomidas` (`idPermiso`, `idComida`, `idpermisoscomidas`) VALUES
(1, 1, 1),
(1, 2, 2),
(1, 3, 3),
(1, 4, 4),
(2, 1, 5),
(2, 2, 6),
(3, 4, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rango`
--

CREATE TABLE IF NOT EXISTS `rango` (
`idRango` int(11) NOT NULL,
  `rango` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `rango`
--

INSERT INTO `rango` (`idRango`, `rango`) VALUES
(1, 'Interno'),
(2, 'Administrativo'),
(3, 'Residente'),
(4, 'Enfermeria');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro`
--

CREATE TABLE IF NOT EXISTS `registro` (
`idRegistro` int(11) NOT NULL,
  `idComensal` varchar(25) DEFAULT NULL,
  `idComida` int(11) DEFAULT NULL,
  `horaComida` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (
`userId` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `nivelacceso` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`userId`, `username`, `password`, `nivelacceso`) VALUES
(1, 'jorge', '1234', 1),
(2, 'shago', '1234', 2),
(4, 'eclipse', '24681012', 1),
(5, 'turno1', '1234', 2),
(6, 'turno2', '1357', 2),
(7, 'turno3', '2468', 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `comensales`
--
ALTER TABLE `comensales`
 ADD PRIMARY KEY (`CURP`);

--
-- Indices de la tabla `comidas`
--
ALTER TABLE `comidas`
 ADD PRIMARY KEY (`idComida`);

--
-- Indices de la tabla `permisos`
--
ALTER TABLE `permisos`
 ADD PRIMARY KEY (`permisoId`);

--
-- Indices de la tabla `permisoscomidas`
--
ALTER TABLE `permisoscomidas`
 ADD PRIMARY KEY (`idpermisoscomidas`);

--
-- Indices de la tabla `rango`
--
ALTER TABLE `rango`
 ADD PRIMARY KEY (`idRango`);

--
-- Indices de la tabla `registro`
--
ALTER TABLE `registro`
 ADD PRIMARY KEY (`idRegistro`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
 ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `permisos`
--
ALTER TABLE `permisos`
MODIFY `permisoId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `rango`
--
ALTER TABLE `rango`
MODIFY `idRango` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT de la tabla `registro`
--
ALTER TABLE `registro`
MODIFY `idRegistro` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=60;
--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
