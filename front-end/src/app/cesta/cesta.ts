import { Component } from '@angular/core';
import { ItemCesta } from '../model/item-cesta';
import { CommonModule } from '@angular/common';


@Component({
  imports: [CommonModule],
  selector: 'app-cesta',
  styleUrl: './cesta.css',
  templateUrl: './cesta.html',
})
export class Cesta {
  mensagem: string = "";
  valorCesta:number = 0;

  itens: ItemCesta[] = [
  {
    "produto": {
      "codigo": 1,
      "nome": "Martelo de Unha 25mm",
      "descritivo": "Martelo com cabo de madeira e cabeça de aço, ideal para trabalhos gerais.",
      "quantidade": 25,
      "valor": 39.90,
      "promo": 29.00,
      "destaque": 1
    },
    "quantidade": 2,
    "valorTotal": 58.00
  },
  {
    "produto": {
      "codigo": 2,
      "nome": "Chave de Fenda 6x150mm",
      "descritivo": "Chave de fenda com ponta resistente e cabo ergonômico.",
      "quantidade": 40,
      "valor": 18.50,
      "promo": 0,
      "destaque": 0
    },
    "quantidade": 3,
    "valorTotal": 55.50
  },
  {
    "produto": {
      "codigo": 3,
      "nome": "Jogo de Chaves Allen",
      "descritivo": "Kit com 9 chaves Allen de diferentes medidas para manutenção e montagem.",
      "quantidade": 18,
      "valor": 32.90,
      "promo": 30.00,
      "destaque": 1
    },
    "quantidade": 1,
    "valorTotal": 30.00
  }
];

  ngOnInit(){
      this.calculaTotal();
  }

  calculaTotal(){
    let soma = 0;
    for (let item of this.itens) {
      soma += this.subtotal(item);
    }
    return soma;
  }

  aumentar(item: ItemCesta) {
    item.quantidade++;
  }

  diminuir(item: ItemCesta) {
    if (item.quantidade > 1) {
      item.quantidade--;
    } else {
      this.remover(item);
    }
  }

  remover(item: ItemCesta) {
    let index = this.itens.indexOf(item);
    if (index >= 0) {
      this.itens.splice(index, 1);
    }
  }

   subtotal(item: ItemCesta): number {
    let valorUnit = item.produto.promo > 0 ? item.produto.promo : item.produto.valor;
    return valorUnit * item.quantidade;
  }

  limparCarrinho() {
    this.itens = [];
  }

  finalizarCompra() {
    alert("Compra finalizada! Total: R$ " + this.calculaTotal());
    this.limparCarrinho();
  }


}
