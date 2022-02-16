<?php
/**
 * This is project's console commands configuration for Robo task runner.
 *
 * @see http://robo.li/
 */
class RoboFile extends \Robo\Tasks
{
    /**
     * 构建一个独立镜像，只包含api和redis
     */
    public function buildDockerImage()
    {
        $tmp_dir = "/tmp/".md5(__DIR__);
        $this->_copyDir('docker', $tmp_dir.'/app/docker');
        # $this->taskReplaceInFile($tmp_dir.'/app/docker/web/init.sh')->from('# ./redis-server &')->to('./redis-server &')->run();
        
        $this->_copyDir('api', $tmp_dir.'/app/docker/web/api');
        $this->_copyDir('push', $tmp_dir.'/app/docker/web/push');
        // 更新redis为memory
        $this->taskReplaceInFile($tmp_dir.'/app/docker/web/push/ios.yml')->from('addr: "redis:6379"')->to('addr: "127.0.0.1:6379"')->run();
        $this->taskReplaceInFile($tmp_dir.'/app/docker/web/push/clip.yml')->from('addr: "redis:6379"')->to('addr: "127.0.0.1:6379"')->run();
        // $this->_exec("open ".$tmp_dir.'/app/docker/');
        $this->_exec("cd $tmp_dir/app && docker build -f ./docker/web/Dockerfile.serverless -t pushdeercore ./docker/web ");
        $this->_exec("docker tag pushdeercore ccr.ccs.tencentyun.com/ftqq/pushdeercore");
        $this->_exec("docker push ccr.ccs.tencentyun.com/ftqq/pushdeercore");
    }
}
