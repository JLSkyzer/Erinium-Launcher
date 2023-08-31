<?php
const FILES_URL = 'https://erinium.000webhostapp.com/files';
const FILES_FOLDER = __DIR__ . '/files';
$json = [
    'extfiles' => [],
    'mods' => []
];

function scanDirectory($directory) {
    global $json;
    $files = array_diff(scandir($directory), ['.', '..']);
    foreach ($files as $file) {
        $fullPath = $directory . '/' . $file;
        if (is_file($fullPath)) {
            if (pathinfo($file, PATHINFO_EXTENSION) === 'jar') {
                $fileInfo = [
                    'name' => $file,
                    'downloadURL' => FILES_URL . '/' . substr($fullPath, strlen(FILES_FOLDER) + 1),
                    'sha1' => sha1_file($fullPath),
                    'size' => filesize($fullPath)
                ];
                $json['mods'][] = $fileInfo;
            } else {
                $fileInfo = [
                    'path' => substr($fullPath, strlen(FILES_FOLDER) + 1),
                    'downloadURL' => FILES_URL . '/' . substr($fullPath, strlen(FILES_FOLDER) + 1),
                    'sha1' => sha1_file($fullPath),
                    'size' => filesize($fullPath)
                ];
                $json['extfiles'][] = $fileInfo;
            }
        } elseif (is_dir($fullPath)) {
            scanDirectory($fullPath);
        }
    }
}

scanDirectory(FILES_FOLDER);

header('Content-Type: application/json');
echo json_encode($json);
?>
