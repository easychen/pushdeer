<?php
/**
 * This is project's console commands configuration for Robo task runner.
 *
 * @see http://robo.li/
 */
class RoboFile extends \Robo\Tasks
{
    // define public methods as commands
    public function buildImage()
    {
        $this->_exec("docker build -t pushdeeresp ./ ");
        $this->_exec("docker tag pushdeeresp ccr.ccs.tencentyun.com/ftqq/pushdeeresp");
        $this->_exec("docker push ccr.ccs.tencentyun.com/ftqq/pushdeeresp");
    }
}
