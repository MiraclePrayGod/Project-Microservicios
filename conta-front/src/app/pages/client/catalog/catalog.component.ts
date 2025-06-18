import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../../core/services/producto.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.scss']
})
export class CatalogComponent implements OnInit {
  productos: any[] = [];
  loading = true;

  constructor(
    private productoService: ProductoService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.productoService.getProductos().subscribe({
      next: (data) => {
        this.productos = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        this.loading = false;
      }
    });
  }

  comprar(producto: any) {
    const clienteId = localStorage.getItem('clienteId');
    const token = localStorage.getItem('token');

    if (!clienteId || !token) {
      this.router.navigate(['/auth/login']);
      return;
    }

    const ventaData = {
      clienteId: Number(clienteId),
      detalles: [
        {
          productoId: producto.id,
          cantidad: 1
        }
      ]
    };

    this.http.post<any>('http://localhost:8080/ventas', ventaData, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: (venta) => {
        const pagoData = {
          ventaId: venta.id,
          metodo: 'TRANSFERENCIA',
          estado: 'PENDIENTE',
          monto: producto.precio
        };

       
      },
      error: (err) => {
        console.error('Error al registrar la venta:', err);
      }
    });
  }

  Pago(Pago: any) {
    this.router.navigate(['/client/pago']);
  }

}
