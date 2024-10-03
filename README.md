## simulador de filas

todo:
* importar configuraçoes de arquivo yaml
* definir formato do arquivo yaml > preferencialmente com a notaçao apresentada no moodle (tipo g/g/1/2)
  * cada fila pode ter de 1 a n destinos
			* cada fila destino tem uma probabildade de ser escolhida
			* a fila pode ter destino "saida", onde o cliente sai do sistema
			* destino saida é uma das opçoes além de outras filas
	*cada fila tem seus tempos maximos e nínimos de chegada e de saída próprio


* implementar comportamento tandem : encadear uma fila na outra
* implementar comportamento de probabilidades de transferencia de fila: uma fila pode ser encadeada em outras, com determinadas probabilidades para cada fila ou até mesmo saída do sistema.
