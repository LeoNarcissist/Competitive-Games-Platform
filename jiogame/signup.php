<?php
# Its servicing in JSON format
header('Content-Type: application/json');

ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

$sqlconnector_script = './sql_connector.php';

# API Format:
# localhost/jiogame/signup.php?fname=ben&lname=dover&email=ben@dover.com&passwd=pass@123

# Extract GET values
$param = array();
foreach ($_GET as $name => $value) {
  $param[$name] = $value;
}

# var_dump($param);

# Include the database handler
require_once($sqlconnector_script);

$insert_user = "INSERT INTO users(fname, lname, email, passwd) VALUES(
    NULLIF('{$param['fname']}',''), 
    NULLIF('{$param['lname']}',''), 
    NULLIF('{$param['email']}',''),
    NULLIF(md5('{$param['passwd']}'),'')
);";

if ($dbc->query($insert_user))
    $uid = $dbc->insert_id;
else
    $uid = null;

# Generate json
class userid{
    public $uid;
    function __construct($uid){
        $this->uid = $uid;
    }
}
$jsonvar = new userid($uid);

echo json_encode($jsonvar);

$dbc->close();

?>
