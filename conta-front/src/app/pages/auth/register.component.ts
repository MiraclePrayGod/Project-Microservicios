import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {
  form!: FormGroup; // <-- Solo declaramos aquí

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {}

  ngOnInit() {
    // Ahora sí lo inicializamos aquí
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      nombre: ['', Validators.required],
      rucDni: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', Validators.required],
      direccion: ['', Validators.required]
    });
  }

  submit() {
    if (this.form.valid) {
      const authData = {
        username: this.form.value.username,
        password: this.form.value.password,
        rol: 'CLIENTE'
      };

      // Paso 1: Registrar en auth-service y guardar el token
      this.http.post<{ token: string }>('http://localhost:8087/auth/register', authData).subscribe((response) => {
        localStorage.setItem('token', response.token);  // ← Guardamos el token

        // Paso 2: Registrar en pd-cliente
        const clienteData = {
          nombre: this.form.value.nombre,
          rucDni: this.form.value.rucDni,
          email: this.form.value.email,
          telefono: this.form.value.telefono,
          direccion: this.form.value.direccion,
          estado: 'ACTIVO'
        };

        this.http.post('/8080/clientes', clienteData, {
          headers: {
            Authorization: `Bearer ${response.token}`  // ← Enviamos token
          }
        }).subscribe(() => {
          this.router.navigate(['/client/catalog']);
        });
      });
    }
  }

}
