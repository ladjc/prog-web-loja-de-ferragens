import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { ItemCesta } from '../model/item-cesta';
import { CommonModule, isPlatformBrowser } from '@angular/common';


@Component({
  imports: [CommonModule],
  selector: 'app-cesta',
  styleUrl: './cesta.css',
  templateUrl: './cesta.html',
})
export class Cesta {

  mensagem: string = "";
  valorCesta: number = 0;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) { }


  itens: ItemCesta[] = [];

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    let json = localStorage.getItem("itemCesta");

    if (json == null) {
      this.mensagem = "Cesta vazia, adicione produtos!";
    } else {
      this.itens = JSON.parse(json);

      if (this.itens.length == 0) {
        this.mensagem = "Cesta vazia, adicione produtos!";
      } else {
        this.mensagem = "";
      }
    }

    this.calculaTotal();
  }

  calculaTotal() {
    let soma = 0;
    for (let item of this.itens) {
      soma += this.subtotal(item);
    }
    return soma;
  }

  aumentar(item: ItemCesta) {
    const preco = item.produto.promo > 0 ? item.produto.promo : item.produto.valor;

    item.quantidade++;
    item.valorTotal = item.quantidade * preco;

    this.salvarCesta();
  }

  diminuir(item: ItemCesta) {
    const preco = item.produto.promo > 0 ? item.produto.promo : item.produto.valor;

    if (item.quantidade > 1) {
      item.quantidade--;
      item.valorTotal = item.quantidade * preco;
      this.salvarCesta();
    } else {
      this.remover(item);
    }
  }

  remover(item: ItemCesta) {
    let index = this.itens.indexOf(item);
    if (index >= 0) {
      this.itens.splice(index, 1);
    }

    this.salvarCesta();
  }

  limparCarrinho() {
    this.itens = [];
    this.salvarCesta();
  }

  salvarCesta() {
    localStorage.setItem("itemCesta", JSON.stringify(this.itens));

    if (this.itens.length == 0) {
      this.mensagem = "Cesta vazia, adicione produtos!";
    } else {
      this.mensagem = "";
    }

    this.calculaTotal();
  }

  subtotal(item: ItemCesta): number {
    let valorUnit = item.produto.promo > 0 ? item.produto.promo : item.produto.valor;
    return valorUnit * item.quantidade;
  }

  finalizarCompra() {
    alert("Compra finalizada! Total: R$ " + this.calculaTotal());
    this.limparCarrinho();
  }




}
