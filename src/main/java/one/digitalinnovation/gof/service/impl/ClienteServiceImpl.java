package one.digitalinnovation.gof.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepService viaCepService;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);

	}

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultaCep(cep);
			if (!novoEndereco.equals(null)) {
				enderecoRepository.save(novoEndereco);
			}
			return novoEndereco;
		});

		cliente.setEndereco(endereco);

		clienteRepository.save(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {

		clienteRepository.findById(id).ifPresent(cli -> {
			salvarClienteComCep(cliente);
		});

	}

	@Override
	public void deletar(Long id) {

//		clienteRepository.findById(id).ifPresent(cliente -> {
//			clienteRepository.delete(cliente);
//		});
		clienteRepository.deleteById(id);

	}

}
