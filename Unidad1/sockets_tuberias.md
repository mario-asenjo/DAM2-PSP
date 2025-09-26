# 📘 Apuntes Bloque 6: Tuberías con nombre y Sockets

## 🔹 Tuberías (Pipes y FIFOs)

- **Pipe anónima:** creada con `|` o `pipe()`. Solo funciona entre procesos emparentados (padre-hijo).
- **FIFO (named pipe):** archivo especial en el sistema de ficheros que permite comunicación entre procesos no relacionados.

Características:
- Comunicación **unidireccional**.
- Implementada como un **buffer circular en memoria** gestionado por el kernel.
- Bloqueo automático:
  - `read()` bloquea si no hay datos.
  - `write()` bloquea si no hay lectores, y genera `SIGPIPE` si se intenta escribir.

### Creación y uso
```bash
mkfifo cola
cat < cola          # lector bloqueado
echo "hola" > cola  # escritor desbloquea al lector
```

### Syscalls principales
- `mknod("cola", S_IFIFO)`
- `open("cola", O_RDONLY / O_WRONLY)`
- `read()`, `write()`
- `close()`

### Flujo típico FIFO

```mermaid
sequenceDiagram
    participant P1 as Proceso lector (cat)
    participant P2 as Proceso escritor (echo)
    participant K as Kernel (FIFO buffer)

    P1->>K: open("cola", O_RDONLY) ← bloquea
    P2->>K: open("cola", O_WRONLY) ← bloquea
    Note over K: Ambos se desbloquean al coincidir lector/escritor
    P2->>K: write("hola")
    K->>P1: desbloquea read()
    P1->>P1: write(1, "hola") → imprime
    P2->>K: close()
    K->>P1: siguiente read() devuelve EOF
```

---

## 🔹 Sockets

- Mecanismo IPC más general que pipes.
- Comunicación **bidireccional**.
- Pueden ser:
  - **Locales:** `AF_UNIX`
  - **Red (TCP/IP):** `AF_INET`, `AF_INET6`

Características:
- Cada socket es un **file descriptor**.
- El kernel mantiene dos buffers: **send** y **recv**.
- Soportan **múltiples clientes** mediante `accept()`.

### Ejemplo con `nc` (netcat)

**Servidor:**
```bash
nc -l -p 12345
```

**Cliente:**
```bash
nc localhost 12345
```

### Syscalls principales

Servidor:
- `socket(AF_INET, SOCK_STREAM, 0)`
- `bind()`
- `listen()`
- `accept()`
- `read()`, `write()`

Cliente:
- `socket(AF_INET, SOCK_STREAM, 0)`
- `connect()`
- `read()`, `write()`

### Flujo típico Socket TCP

```mermaid
sequenceDiagram
    participant S as Servidor (nc -l)
    participant C as Cliente (nc)
    participant K as Kernel (TCP/IP)

    S->>K: socket()
    S->>K: bind(12345)
    S->>K: listen()
    S->>K: accept() ← bloquea

    C->>K: socket()
    C->>K: connect(127.0.0.1:12345)

    Note over K: Handshake TCP (SYN, SYN-ACK, ACK)

    K->>S: accept() devuelve nuevo FD

    C->>K: write("hola")
    K->>S: read() devuelve "hola"

    S->>C: write("adiós")
    K->>C: read() devuelve "adiós"
```

---

## 🔹 Comparativa FIFO vs Socket

| Aspecto             | FIFO (mkfifo)                        | Socket (nc / API)                      |
|---------------------|--------------------------------------|----------------------------------------|
| Dirección           | Unidireccional                       | Bidireccional                          |
| Alcance             | Solo local (sistema de ficheros)     | Local (`AF_UNIX`) o red (`AF_INET`)    |
| Persistencia        | Archivo especial en FS               | No hay archivo (solo puerto/socket)    |
| Multiplicidad       | 1 escritor ↔ 1 lector (no broadcast) | Varios clientes con `accept()`         |
| Buffers             | Uno compartido                       | Dos buffers (send/recv)                |

---

## 🔹 Analogía

- **FIFO:** un tubo de plástico → lo que entra por un extremo sale por el otro, en orden FIFO.
- **Socket:** un teléfono → puedes hablar y escuchar a la vez, y tener muchos conectados a la centralita.

---
