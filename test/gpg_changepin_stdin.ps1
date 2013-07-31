$wshell = new-object -com wscript.shell
$wshell.run("gpg --change-pin")
Start-Sleep 3
$wshell.sendkeys("1{enter}")
Start-Sleep 1
$wshell.sendkeys("123456{enter}")
Start-Sleep 1
$wshell.sendkeys("654321{enter}")
Start-Sleep 1
$wshell.sendkeys("654321{enter}")
Start-Sleep 1
$wshell.sendkeys("q{enter}")

