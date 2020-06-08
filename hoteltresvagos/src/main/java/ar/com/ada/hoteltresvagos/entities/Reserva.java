package ar.com.ada.hoteltresvagos.entities;

import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.NaturalId;
import ar.com.ada.hoteltresvagos.excepciones.*;

@Entity
@Table(name="reserva")
public class Reserva {

    @Id
    @Column(name="reserva_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservaId;
    @Column(name = "fecha_reserva")
    private Date fechaReserva;
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    @Column(name = "fecha_egreso")
    private Date fechaEgreso;
    private Integer habitacion; //int es primitivo e integer es el objeto entero, asi se puede ser null
    @Column(name = "importe_reserva")
    private BigDecimal importeReserva;
    @Column(name = "Importe_total")
    private BigDecimal importeTotal;
    @Column(name = "importe_pagado")
    private BigDecimal importePagado;
    @Column(name = "tipo_estado_id")
    private int tipoEstadoId; //por ahora
    @ManyToOne
    @JoinColumn(name = "huesped_id", referencedColumnName = "huesped_id")

    private Huesped huesped;

	public int getReservaId() {
		return reservaId;
	}

	public void setReservaId(int reservaId) {
		this.reservaId = reservaId;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public Integer getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Integer habitacion) {
		this.habitacion = habitacion;
	}

	public BigDecimal getImporteReserva() {
		return importeReserva;
	}

	public void setImporteReserva(BigDecimal importeReserva) {
		this.importeReserva = importeReserva;
	}

	public BigDecimal getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public int getTipoEstadoId() {
		return tipoEstadoId;
	}

	public void setTipoEstadoId(int tipoEstadoId) {
		this.tipoEstadoId = tipoEstadoId;
	}

	public Huesped getHuesped() {
		return huesped;
	}

	public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
        this.huesped.getReservas().add(this);
	}
    
}