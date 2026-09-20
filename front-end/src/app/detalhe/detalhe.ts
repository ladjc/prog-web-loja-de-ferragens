import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Produto } from '../model/produto';

@Component({
  imports: [CommonModule],
  selector: 'app-detalhe',
  styleUrl: './detalhe.css',
  templateUrl: './detalhe.html',
})
export class Detalhe implements OnInit {
  obj: Produto = new Produto();
  mensagem: string = "";

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    let json = localStorage.getItem("produto");
    if (json == null) {
      this.mensagem = "Produto Invalido, verifique!";
    } else {
      this.mensagem = "";
      this.obj = JSON.parse(json);
    }
  }
}