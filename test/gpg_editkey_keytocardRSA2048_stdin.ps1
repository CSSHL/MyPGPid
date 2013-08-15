$wshell = new-object -com wscript.shell
$wshell.run("gpg --edit-key pcrsa2048")
Start-Sleep 1
$wshell.sendkeys("toggle{enter}")
Start-Sleep 1
$wshell.sendkeys("keytocard{enter}")
Start-Sleep 1
$wshell.sendkeys("y{enter}")
Start-Sleep 2
$wshell.sendkeys("1{enter}")
Start-Sleep 1
$wshell.sendkeys("test1{enter}")
Start-Sleep 1
$wshell.sendkeys("12345678{enter}")
