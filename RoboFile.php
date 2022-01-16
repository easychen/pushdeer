<?php
/**
 * This is project's console commands configuration for Robo task runner.
 *
 * @see http://robo.li/
 */
class RoboFile extends \Robo\Tasks
{
    // define public methods as commands
    public function buildDockerImage()
    {
        $tmp_dir = "/tmp/".md5(__DIR__);
        $this->_copyDir('docker', $tmp_dir.'/app/docker');
        $this->_copyDir('api', $tmp_dir.'/app/docker/web/api');
        $this->_copyDir('push', $tmp_dir.'/app/docker/web/push');
        $this->_exec("cd $tmp_dir/app && docker build -f ./docker/web/dockerfile.serverless -t pushdeercore ./docker/web ");
        $this->_exec("docker tag pushdeercore ccr.ccs.tencentyun.com/ftqq/pushdeercore");
        $this->_exec("docker push ccr.ccs.tencentyun.com/ftqq/pushdeercore");
    }
}
