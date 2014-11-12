<?php

/**
 * Created by PhpStorm.
 * User: funivan
 * Date: 11/12/14
 * Time: 9:45 AM
 */
class A
{
    public function setItems()
    {
        $this->set('doc', 'test');
        $user = new User();
        $user->set('title', 123);
        $this->set('title', 'doc');
        $this->set();
        $this->set('title', 'other');
    }
}

$title = "|1";
$doc = "|1";