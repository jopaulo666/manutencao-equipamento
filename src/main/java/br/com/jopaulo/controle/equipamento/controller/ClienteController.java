package br.com.jopaulo.controle.equipamento.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.jopaulo.controle.equipamento.model.Cliente;
import br.com.jopaulo.controle.equipamento.repository.ClienteRepository;

@Controller
public class ClienteController {
	
	@Autowired
	private ClienteRepository  clienteRepository;
	
	@Autowired
	private ReportUtil reportUtil;

	@RequestMapping(method = RequestMethod.GET, value = "**/cadastro-cliente")
	public ModelAndView inicio() {		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clienteobj", new Cliente());
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))));
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarcliente", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		if (bindingResult.hasErrors()) {
			ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
			andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))));
			andView.addObject("clienteobj", cliente);
			
			List<String> msg = new ArrayList<String>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			andView.addObject("msg", msg);
			return andView;
		}
		
		if (file.getSize() > 0) {
			cliente.setFoto(file.getBytes());
			cliente.setNomeFileFoto(file.getOriginalFilename());
			cliente.setTipoFileFoto(file.getContentType());
		} else {
			if (cliente.getId() != null && cliente.getId() > 0) { // editando
				Cliente clienteTemp = clienteRepository.findById(cliente.getId()).get();
				cliente.setFoto(clienteTemp.getFoto());
				cliente.setNomeFileFoto(clienteTemp.getNomeFileFoto());
				cliente.setTipoFileFoto(clienteTemp.getTipoFileFoto());
			}
		}
		
		clienteRepository.save(cliente);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))));
		andView.addObject("clienteobj", new Cliente());
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastro-cliente");
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))));
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
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))));
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
	
	@GetMapping("**/pesquisarcliente")
	public void relatorio(@RequestParam("nomepesquisa") String nomepesquisa, 
			@RequestParam("pesqsituacao") String pesqsituacao,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		if (pesqsituacao != null && !pesqsituacao.isEmpty() && nomepesquisa != null && !nomepesquisa.isEmpty()) {
			clientes = clienteRepository.findByNameSituacaoContainingIgnoreCase(nomepesquisa, pesqsituacao);
		}else if (nomepesquisa != null && !nomepesquisa.isEmpty()) {
			clientes = clienteRepository.findByNameContainingIgnoreCase(nomepesquisa);
		} else if (pesqsituacao != null && !pesqsituacao.isEmpty()) {
			clientes = clienteRepository.findBySituacao(pesqsituacao);
		} else {
			Iterable<Cliente> iterable = clienteRepository.findAll();
			for (Cliente cliente : iterable) {
				clientes.add(cliente);
			}
		}
		
		// chama o serviço que faz a geração do pdf
		byte[] pdf = reportUtil.gerarRelatorio(clientes, "cliente", request.getServletContext());
		
		// tamanho da resposta
		response.setContentLength(pdf.length);
		
		//tipo de arquivo (nesse caso, todos)
		response.setContentType("application/octet-stream");
		
		// Define o cabeçalho da resposta
		String headerKey = "Content-Disposition";		
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		// finaliza a resposta para o navegador
		response.getOutputStream().write(pdf);
		
		
	}
	
	@GetMapping("/detalhecliente/{idcliente}")
	public ModelAndView detalheCliente(@PathVariable("idcliente") Long idcliente) {
		
		Optional<Cliente> cliente = clienteRepository.findById(idcliente);
		
		ModelAndView andView = new ModelAndView("cadastro/detalhe-cliente");
		andView.addObject("clienteobj", cliente.get());
		
		return andView;
	}
	
	@GetMapping("**/baixarfoto/{idcliente}")
	public void baixarFoto(@PathVariable("idcliente") Long idcliente, HttpServletResponse response) throws IOException {
		
		Cliente cliente = clienteRepository.findById(idcliente).get();
		
		if (cliente.getFoto() != null) {
			response.setContentLength(cliente.getFoto().length); // tamanho
			response.setContentType(cliente.getTipoFileFoto()); // tipo
			String headerKey = "Content-Disposition";		    // resposta
			String headerValue = String.format("attachment; filename=\"%s\"", cliente.getNomeFileFoto());
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(cliente.getFoto()); // finaliza passando o arquivo
		}
		
		
	}
}
