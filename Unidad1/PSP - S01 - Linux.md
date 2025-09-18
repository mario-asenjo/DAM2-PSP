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

```
    
11. Finaliza el proceso de Firefox usando su PID.
```bash

```
    
12. Vuelve a lanzarlo y esta vez deténlo, luego reactívalo.
```bash

```
    
13. Crea un script que capture la señal de interrupción (Ctrl+C) y muestre un mensaje en lugar de cerrarse.
```bash

```
    

---

## **Bloque 3: Gestión de servicios con systemd**

14. Consulta el estado del servicio de conexión remota (por ejemplo, `ssh`).
```bash

```
    
15. Inicia dicho servicio si está instalado.
```bash

```
    
16. Desactívalo del arranque automático y vuelve a activarlo.
```bash

```
    

---

## **Bloque 4: Archivos y directorios**

17. Lista todos los archivos, incluidos los ocultos, en tu directorio personal.
```bash

```
    
18. Crea una carpeta llamada `prueba`.
```bash

```
    
19. Dentro de esa carpeta, crea un archivo `notas.txt` que contenga el texto “Hola Linux”.
```bash

```
    
20. Copia ese archivo con otro nombre.
```bash

```
    
21. Renombra el archivo copiado.
```bash

```
    
22. Borra el archivo renombrado.
```bash

```
    

---

## **Bloque 5: Redirecciones y pipes**

23. Redirige la salida de un listado de archivos a un archivo llamado `listado.txt`.
```bash

```
    
24. Añade una nueva línea al final del mismo archivo con el texto "Fin del listado".
```bash

```
    
25. Redirige los errores (2) de una operación no válida (`let a=3/0`) a un dispositivo nulo para ignorarlos.
```bash

```
    
26. Filtra de una lista de procesos únicamente aquellos que contengan la palabra “bash”.
```bash

```
    
27. Muestra solo las últimas 5 líneas del archivo `listado.txt`.
```bash

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

```
    
33. Muestra la configuración de tus interfaces de red.
```bash

```
    
34. Revisa qué puertos están en escucha en tu máquina.
```bash

```
    
35. Consulta la dirección IP asociada al dominio `google.com`.
```bash

```
    
36. Realiza la misma consulta de resolución DNS usando otra herramienta distinta.
```bash

```
    
37. Conéctate de forma remota a otra máquina mediante un protocolo seguro (si tienes acceso).
```bash

```
    
38. Copia un archivo desde tu máquina a otra mediante una conexión remota segura.
```bash

```
    

---

## **Bloque 8: Usuarios y permisos**

39. Crea un usuario de prueba llamado `alumno1`.
```bash

```
    
40. Cámbiale la contraseña.
```bash

```
    
41. Cambia los permisos de un archivo a `755`.
```bash

```
    
42. Cambia el propietario de un archivo a otro usuario.
```bash

```
    
43. Elimina el usuario creado.
```bash

```
    

---

