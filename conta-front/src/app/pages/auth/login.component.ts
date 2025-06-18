import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  error: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit() {
    if (this.form.valid) {
      this.http.post<any>('http://localhost:8087/auth/login', this.form.value).subscribe({
        next: (res) => {
          const token = res.token;
          const email = res.email;

          if (!email || !token) {
            this.error = 'Token inválido';
            return;
          }

          localStorage.setItem('token', token);

          // Obtener todos los clientes y buscar por email
          this.http.get<any[]>('http://localhost:8080/clientes').subscribe({
            next: (clientes) => {
              const cliente = clientes.find(c => c.email === email);
              if (cliente) {
                localStorage.setItem('clienteId', cliente.id);
                this.router.navigate(['/client/catalog']);
              } else {
                this.error = 'No se encontró el cliente con este correo.';
              }
            },
            error: () => {
              this.error = 'Error al obtener los clientes.';
            }
          });
        },
        error: () => {
          this.error = 'Usuario o contraseña incorrectos';
        }
      });
    }
  }

  register(register: any) {
    this.router.navigate(['/auth/register']);
  }
}
