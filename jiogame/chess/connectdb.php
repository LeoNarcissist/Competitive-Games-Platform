<?php
	// $Id: connectdb.php,v 1.4 2010/08/14 16:57:54 sandking Exp $

/*
    This file is part of WebChess. http://webchess.sourceforge.net
	Copyright 2010 Jonathan Evraire, Rodrigo Flores

    WebChess is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WebChess is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WebChess.  If not, see <http://www.gnu.org/licenses/>.
*/
ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

	/* load settings */
	if (!isset($_CONFIG))
        require 'config.php';
        
	/* connect to database */
    $dbc = new mysqli($CFG_SERVER, $CFG_USER, $CFG_PASSWORD, $CFG_DATABASE);

if($dbc->connect_errno) {
	die('WebChess cannot connect to the database.  Please check the database settings in your config. <br />' . $dbc->connect_error);
}
?>
