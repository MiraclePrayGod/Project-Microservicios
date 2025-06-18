// src/app/pages/pago/pago.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pago',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pago.component.html'
})
export class PagoComponent implements OnInit {
  form!: FormGroup;
  clienteId!: number;
  mensaje: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.clienteId = Number(localStorage.getItem('clienteId'));
    this.form = this.fb.group({
      metodoPago: ['', Validators.required],
      monto: [null, [Validators.required, Validators.min(1)]],
      referencia: ['', Validators.required]
    });
  }

  submit() {
    if (this.form.valid) {
      const token = localStorage.getItem('token');
      const pagoData = {
        clienteId: this.clienteId,
        metodoPago: this.form.value.metodoPago,
        monto: this.form.value.monto,
        referencia: this.form.value.referencia
      };

      this.http.post('http://localhost:8080/ventas/pagos', pagoData, {
        headers: { Authorization: `Bearer ${token}` }
      }).subscribe({
        next: () => {
          this.mensaje = 'Pago registrado con éxito. Revisa tu correo.';
          this.form.reset();
        },
        error: () => {
          this.mensaje = 'Error al registrar el pago.';
        }
      });
    }
  }
}
