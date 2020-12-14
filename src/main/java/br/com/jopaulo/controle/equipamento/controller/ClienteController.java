package br.com.jopaulo.controle.equipamento.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.jopaulo.controle.equipamento.model.Cliente;
import br.com.jopaulo.controle.equipamento.repository.ClienteRepository;

@Controller
public class ClienteController {
	
	@Autowired
	private ClienteRepository  clienteRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastro-cliente")
	public ModelAndView inicio() {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clienteobj", new Cliente());
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarcliente")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
			Iterable<Cliente> clientesIt = clienteRepository.findAll();
			andView.addObject("clientes", clientesIt);
			andView.addObject("clienteobj", cliente);
			
			List<String> msg = new ArrayList<String>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			andView.addObject("msg", msg);
			return andView;
		}
		
		clienteRepository.save(cliente);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		Iterable<Cliente> clientesIt = clienteRepository.findAll();
		andView.addObject("clientes", clientesIt);
		andView.addObject("clienteobj", new Cliente());
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		Iterable<Cliente> clientesIt = clienteRepository.findAll();
		andView.addObject("clientes", clientesIt);
		andView.addObject("clienteobj", new Cliente());
		return andView;
	}
	
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Long idcliente) {
		
		Optional<Cliente> cliente = clienteRepository.findById(idcliente);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clienteobj", cliente.get());
		
		return andView;
	}
	
	@GetMapping("/excluircliente/{idcliente}")
	public ModelAndView excluir(@PathVariable("idcliente") Long idcliente) {
		
		clienteRepository.deleteById(idcliente);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clientes", clienteRepository.findAll());
		andView.addObject("clienteobj", new Cliente());
		
		return andView;
	}
	
	@PostMapping("**/pesquisarcliente")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, @RequestParam("pesqsituacao") String pesqsituacao) {
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		if (pesqsituacao != null && !pesqsituacao.isEmpty()) {
			clientes = clienteRepository.findByNameSituacaoContainingIgnoreCase(nomepesquisa, pesqsituacao);
		} else {
			clientes = clienteRepository.findByNameContainingIgnoreCase(nomepesquisa);
		}
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clientes", clientes);
		andView.addObject("clienteobj", new Cliente());
		
		return andView;
		
	}
	
	@GetMapping("/detalhecliente/{idcliente}")
	public ModelAndView dedetalheCliente(@PathVariable("idcliente") Long idcliente) {
		
		Optional<Cliente> cliente = clienteRepository.findById(idcliente);
		
		ModelAndView andView = new ModelAndView("cadastro/detalhe-cliente");
		andView.addObject("clienteobj", cliente.get());
		
		return andView;
	}
}
