
<?php

include 'Connec.php';
$pdo = Connec::connect();
$opc = $_GET['opc'];
//If opc equals 1 : list all item in the table
if ($opc == 1) {
    $usuarios = array();
    $sql = 'SELECT * FROM usuario where estado=1 ORDER BY id DESC';
    foreach ($pdo->query($sql) as $row) {

        $usuarios[] = array('id' => $row['id'], 'usuario' => $row['usuario'], 'clave' => $row['clave'], 'estado' => $row['estado']);
    }

    Connec::disconnect();
    $json_string = json_encode($usuarios);
    echo $json_string;
}
//If opc equals 2 : insert item in  the table
if ($opc == 2) {

    $usuario = $_GET['usuario'];
    $clave = $_GET['clave'];
    $estado = 1;
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "insert  into usuario values(null,?,?,?)";
    $q = $pdo->prepare($sql);
    $in = $q->execute(array($usuario, $clave, $estado));
    echo json_encode(array('value' => $in));
    Connec::disconnect();
}

//If opc equals 3 : update item  in  the table
if ($opc == 3) {
    $id = $_GET['id'];
    $usuario = $_GET['usuario'];
    $clave = $_GET['clave'];
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "update usuario set usuario=?, clave=? where id=?";
    $q = $pdo->prepare($sql);
    $in = $q->execute(array($usuario, $clave,$id));
    echo json_encode(array('value' => $in));
    Connec::disconnect();
}
//If opc equals 4 : delete item in  the table

if ($opc == 4) {
    $id = $_GET['id'];    
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "delete from  usuario where id=?";
    $q = $pdo->prepare($sql);
    $in = $q->execute(array($id));
    echo json_encode(array('value' => $in));
    Connec::disconnect();
}
?>
