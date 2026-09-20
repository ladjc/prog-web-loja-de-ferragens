import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Produto } from '../model/produto';

@Component({
  imports: [CommonModule],
  selector: 'app-detalhe',
  styleUrl: './detalhe.css',
  templateUrl: './detalhe.html',
})
export class Detalhe {
  obj:Produto = new Produto();
  mensagem:string = "";

  ngOnInit(){
    let json = localStorage.getItem("produto");
    if(json==null){
      this.mensagem = "Produto Invalido, verifique!";
    }  else {
      this.mensagem = "";
      this.obj = JSON.parse(json);
    }
  }

}
