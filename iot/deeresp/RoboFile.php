<?php
/**
 * This is project's console commands configuration for Robo task runner.
 *
 * @see http://robo.li/
 */
class RoboFile extends \Robo\Tasks
{
    // define public methods as commands
    public function text2hex()
    {
        $chars = utf8Split(file_get_contents('font.txt'));
        $chars = array_unique($chars);
        foreach ($chars as $char) {
            if (strlen($char) > 0) {
                echo '0x'.dechex(uniord($char)).',';
            }
            // break;
        }
    }
}
function utf8Split($str, $len = 1)
{
    $arr = array();
    $strLen = mb_strlen($str, 'UTF-8');
    for ($i = 0; $i < $strLen; $i++) {
        $arr[] = mb_substr($str, $i, $len, 'UTF-8');
    }
    return $arr;
}

function uniord($u)
{
    $k = mb_convert_encoding($u, 'UCS-2LE', 'UTF-8');
    $k1 = ord(substr($k, 0, 1));
    $k2 = ord(substr($k, 1, 1));
    return $k2 * 256 + $k1;
}
