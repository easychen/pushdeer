<?php
/**
 * This is project's console commands configuration for Robo task runner.
 *
 * @see http://robo.li/
 */
class RoboFile extends \Robo\Tasks
{
    // define public methods as commands
    public function buildMac()
    {
        $this->_exec("cd src && go build -o ../bin/gorush.mac");
    }

    public function testMac()
    {
        $this->_exec("bin/gorush.mac -c ../src/config/testdata/config.online.yml");
    }

    public function buildLinux()
    {
        $this->_exec("cd src && CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o ../../../docker/web/gorush");
    }
}
