## **Bloque 0: Historial**

0. Amplía el tamaño máximo del historial de la shell y configura que cada línea del historial muestre también la fecha y la hora.

```bash
export HISTSIZE=10000
export HISTFILESIZE=20000
export HISTTIMEFORMAT='%F %T'
```

1. Comprueba que ahora, al mostrar el historial, aparece la fecha y hora junto a los comandos ejecutados.

```bash
history
mario@psp-ubuntu:~$ history
1  2025-09-18 08:33:08 clear
2  2025-09-18 08:33:24 man history
3  2025-09-18 08:41:17 export HISTSIZE=10000
...
```

2. Configura estos cambios para que sean permantentes
```bash
echo 'export HISTSIZE=10000' >> ~/.bashrc
echo 'export HISTFILESIZE=20000' >> ~/.bashrc
echo "export HISTTIMEFORMAT='%F %T'" >> ~/.bashrc
```

---

## **Bloque 1: Gestión del sistema y el kernel**

1. Muestra toda la información de tu sistema operativo y kernel.
```bash
uname -a
```
    
2. Averigua únicamente la versión del kernel.
```bash
uname -r
```
    
3. Comprueba el espacio en disco disponible.
```bash
df -h
```
    
4. Calcula cuánto espacio ocupa la carpeta `/etc`.
```bash
du -sh /etc
```
    
5. Consulta la memoria RAM disponible y usada.
```bash
free -h
```
    

---

## **Bloque 2: Procesos**

6. Lanza un monitor de procesos en tiempo real y observa:
```bash
ps aux
# o
top
# o
htop
```
    
- Número total de procesos.
    Comando `top` y arriba a la izquierda `Tasks: 164`
- Cuál consume más CPU.
	Comando `top` y ejecutamos `CTRL+M` para ordenar por consumo de memoria, teniendo el primer resultado arriba del todo.
- Sal del programa.
    Comando `top` pulsar la tecla `q`.
7. Instala y ejecuta una versión mejorada del monitor de procesos y compárala con la anterior.
```bash
    sudo apt install -y htop
```
    
8. Obtén un listado de todos los procesos del sistema y localiza el proceso de tu shell.
```bash
ps -lax

#-l listado
#-a ocultos
#-x procesos del usuario actual

ps -lax | grep bash # Localiza el proceso de tu shell

```
    
9. Muestra la jerarquía de procesos en forma de árbol.
```bash
pstree 
# o
htop -t
```
    
10. Lanza el comando `ping` contra `google.com` en segundo plano (&) y obtén su identificador de proceso (PID).
```bash
# Lanzamos el ping en segundo plano escribimos el output en un fichero salida
ping www.google.es >salida &
# Ahora buscamos y filtramos tanto por comando como la columna del PID
ps aux |grep ping| head -n1 | awk '{print "PID de Ping a google.es: " $2}'
```
    
11. Finaliza el proceso de Firefox usando su PID.
```bash
firefox > x &
kill -9 $(ps aux | grep firefox | head -n1 | awk '{ print $2 })'
```
    
12. Vuelve a lanzarlo y esta vez deténlo, luego reactívalo.
```bash
firefox > x &
PID=$(ps aux | grep firefox | head -n1 | awk '{ print $2 })
kill -19 $PID
kill -18 $PID
```
    
13. Crea un script que capture la señal de interrupción (Ctrl+C) y muestre un mensaje en lugar de cerrarse.
```bash

```
    #!/bin/bash

	trap 'echo "Se ha pulsado Ctrl+C y no se cierra...."' SIGINT
	while true; do
		echo "El programa sigue en ejecucion....."
		echo "Introduce Ctrl+C para probarlo....."
		sleep 2
	done
---

## **Bloque 3: Gestión de servicios con systemd**

14. Consulta el estado del servicio de conexión remota (por ejemplo, `ssh`).
```bash
mario@DESKTOP-JP2436Q:~/PSP$ systemctl status ssh
○ ssh.service - OpenBSD Secure Shell server
     Loaded: loaded (/usr/lib/systemd/system/ssh.service; disabled; preset: enabled)
     Active: inactive (dead) since Wed 2025-09-24 19:39:22 CEST; 33s ago
   Duration: 1min 12.092s
TriggeredBy: ○ ssh.socket
       Docs: man:sshd(8)
             man:sshd_config(5)
    Process: 4267 ExecStartPre=/usr/sbin/sshd -t (code=exited, status=0/SUCCESS)
    Process: 4269 ExecStart=/usr/sbin/sshd -D $SSHD_OPTS (code=exited, status=0/SUCCESS)
   Main PID: 4269 (code=exited, status=0/SUCCESS)
        CPU: 15ms

Sep 24 19:38:10 DESKTOP-JP2436Q systemd[1]: Starting ssh.service - OpenBSD Secure Shell server...
Sep 24 19:38:10 DESKTOP-JP2436Q sshd[4269]: Server listening on 0.0.0.0 port 22.
Sep 24 19:38:10 DESKTOP-JP2436Q sshd[4269]: Server listening on :: port 22.
Sep 24 19:38:10 DESKTOP-JP2436Q systemd[1]: Started ssh.service - OpenBSD Secure Shell server.
Sep 24 19:39:22 DESKTOP-JP2436Q sshd[4269]: Received signal 15; terminating.
Sep 24 19:39:22 DESKTOP-JP2436Q systemd[1]: Stopping ssh.service - OpenBSD Secure Shell server...
Sep 24 19:39:22 DESKTOP-JP2436Q systemd[1]: ssh.service: Deactivated successfully.
Sep 24 19:39:22 DESKTOP-JP2436Q systemd[1]: Stopped ssh.service - OpenBSD Secure Shell server.
```
    
15. Inicia dicho servicio si está instalado.
```bash
sudo systemctl start ssh
```
    
16. Desactívalo del arranque automático y vuelve a activarlo.
```bash
sudo systemctl disable ssh
sudo systemctl enable ssh
systemctl is-enabled ssh
---

## **Bloque 4: Archivos y directorios**

17. Lista todos los archivos, incluidos los ocultos, en tu directorio personal.
```bash
ls -la ~
```
    
18. Crea una carpeta llamada `prueba`.
```bash
mkdir prueba
```
    
19. Dentro de esa carpeta, crea un archivo `notas.txt` que contenga el texto “Hola Linux”.
```bash
echo "Hola Linux" > prueba/notas.txt
```
    
20. Copia ese archivo con otro nombre.
```bash
cp prueba/notas.txt pruebas/new_pruebas.txt
```
    
21. Renombra el archivo copiado.
```bash
mv pruebas/new_pruebas.txt pruebas/renamed.txt
```
    
22. Borra el archivo renombrado.
```bash
rm -rf pruebas/renamed.txt
```
    

---

## **Bloque 5: Redirecciones y pipes**

23. Redirige la salida de un listado de archivos a un archivo llamado `listado.txt`.
```bash
ls -l > listado.txt
```
    
24. Añade una nueva línea al final del mismo archivo con el texto "Fin del listado".
```bash
echo "Fin del listado" >> listado.txt
```
    
25. Redirige los errores (2) de una operación no válida (`let a=3/0`) a un dispositivo nulo para ignorarlos.
```bash
let a=3/0 2>/dev/null
```
    
26. Filtra de una lista de procesos únicamente aquellos que contengan la palabra “bash”.
```bash
ps aux | grep bash
```
    
27. Muestra solo las últimas 5 líneas del archivo `listado.txt`.
```bash
cat listado.txt | head -n 5
```
    

---

## **Bloque 6: Tuberías con nombre y sockets**

28. Crea una tubería con nombre llamada `cola`.
```bash

```
    
29. Desde una terminal, deja el archivo `cola` en espera de datos. Desde otra terminal, escribe un mensaje en esa tubería.
```bash

```
    
30. Verifica que `cola` es realmente una tubería.
```bash

```
    
31. Establece un canal de comunicación entre dos terminales locales utilizando una herramienta que permite redirigir flujos de entrada y salida entre sockets.
```bash

```
    

---

## **Bloque 7: Redes**

32. Comprueba la conectividad con el servidor `google.com` enviando unos pocos paquetes.
```bash
ping -c 4 www.google.es
```
    
33. Muestra la configuración de tus interfaces de red.
```bash
ifconfig
```
    
34. Revisa qué puertos están en escucha en tu máquina.
```bash
ss -tuln
# o
netstat -tuln
# o
sudo lsof -i -P -n | grep LISTEN
```
    
35. Consulta la dirección IP asociada al dominio `google.com`.
```bash
dig www.google.com
# o
host www.google.com
```
    
36. Realiza la misma consulta de resolución DNS usando otra herramienta distinta.
```bash
dig www.google.com
# o
host www.google.com
```
    
37. Conéctate de forma remota a otra máquina mediante un protocolo seguro (si tienes acceso).
```bash
ssh user@serverIP
```
    
38. Copia un archivo desde tu máquina a otra mediante una conexión remota segura.
```bash
# de local a remoto
scp [opciones] archivo_local usuario@host:/ruta/remota/
# por ejemplo un archivo ~/.ssh/.id_rsa a /home/mario/.ssh/.
scp ~/.ssh/.id_rsa mario@ubuntuPSP:/home/mario/.ssh/.

# del remoto hacia el local
scp [opciones] usuario@host:/ruta/remota /ruta/local
# por ejemplo
scp mario@ubuntuPSP:/home/mario/file.txt ./file.txt
```
    

---

## **Bloque 8: Usuarios y permisos**

39. Crea un usuario de prueba llamado `alumno1`.
```bash
sudo useradd alumno1
```
    
40. Cámbiale la contraseña.
```bash
sudo passwd alumno1
```
    
41. Cambia los permisos de un archivo a `755`.
```bash
chmod 755 archivo.txt
```
    
42. Cambia el propietario de un archivo a otro usuario.
```bash
chown mario:mario archivo.txt
```
    
43. Elimina el usuario creado.
```bash
sudo userdel alumno1
```
    

---

